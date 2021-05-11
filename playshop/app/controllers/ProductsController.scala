package controllers

import forms.{CreateProductData, DeleteProductData, ProductForms, UpdateProductData}
import models.Product
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import repositories.{CategoryRepository, PartsManufacturerRepository, ProductRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductsController @Inject()(cc: MessagesControllerComponents, val productRepository: ProductRepository, val partsManufacturerRepository: PartsManufacturerRepository, val categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
  def create(categoryId: Long) = Action(parse.json) { request =>
    val result = request.body.validate[CreateProductData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      product => {
        productRepository.create(product.name, product.description, product.price, product.partsManufacturerId, categoryId)
        Ok(Json.obj("message" -> (s"Product ${product.name} created")))
      }
    )
  }

  def getByCategoryId(categoryId: Long): Action[AnyContent] = Action.async { implicit request =>
    if (categoryId == -1)
      productRepository.getAll().map(products => Ok(Json.toJson(products)))
    else
      productRepository.getByCategoryId(categoryId).map(products => Ok(Json.toJson(products)))
  }

  def getById(id: Long): Action[AnyContent] = Action.async { implicit request =>
    productRepository.getByIdOption(id).map(product => product match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.ProductsController.getByCategoryId())
    })
  }

  def update(id: Long) = Action(parse.json) { request =>
    val result = request.body.validate[UpdateProductData]
    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      product => {
        productRepository.update(id, Product(id, product.name, product.description, product.price, product.partsManufacturerId, product.categoryId))
        Ok(Json.obj("message" -> (s"Product updated")))
      }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request =>
    productRepository.delete(id).map(_ => Ok(s"Product $id deleted"))
  }

  def createForm(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    partsManufacturerRepository.getAll().flatMap(partsManufacturers => {
      categoryRepository.getAll().map(categories => Ok(views.html.products.productAdd(ProductForms.CreateForm, partsManufacturers, categories)))
    })
  }

  def createFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    partsManufacturerRepository.getAll().flatMap(partsManufacturers => {
      categoryRepository.getAll().flatMap(categories =>
        ProductForms.CreateForm.bindFromRequest.fold(
          errorForm => Future.successful(BadRequest(views.html.products.productAdd(errorForm, partsManufacturers, categories))),
          productData => {
            productRepository.create(productData.name, productData.description, productData.price, productData.partsManufacturerId, productData.categoryId).map { _ =>
              Redirect(routes.ProductsController.createForm()).flashing("success" -> "Product created")
            }
          }
        )
      )
    })
  }

  def getByCategoryIdForm(categoryId: Long): Action[AnyContent] = Action.async { implicit request =>
    if (categoryId == -1) {
      productRepository.getAll().map(products => Ok(views.html.products.products(products, null)))
    }
    else {
      productRepository.getByCategoryId(categoryId).flatMap(products => {
        categoryRepository.getById(categoryId).map(category => Ok(views.html.products.products(products, category)))
      })
    }
  }

  def getByIdForm(id: Long): Action[AnyContent] = Action.async { implicit request =>
    productRepository.getById(id).flatMap(product => {
      partsManufacturerRepository.getById(product.partsManufacturerId).flatMap(partsManufacturer => {
        categoryRepository.getById(product.categoryId).map(category => Ok(views.html.products.productDetails(product, partsManufacturer, category)))
      })
    })
  }

  def updateForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.getById(id).map(product => {
      val updateForm = ProductForms.UpdateForm.fill(UpdateProductData(product.id, product.name, product.description, product.price, product.partsManufacturerId, product.categoryId))
      Ok(views.html.products.productUpdate(updateForm))
    })
  }

  def updateFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    ProductForms.UpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.products.productUpdate(errorForm)))
      },
      productData => {
        productRepository.update(productData.id, Product(productData.id, productData.name, productData.description, productData.price, productData.partsManufacturerId, productData.categoryId)).map { _ =>
          Redirect(routes.ProductsController.updateForm(productData.id)).flashing("success" -> "Product updated")
        }
      }
    )
  }

  def deleteForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    productRepository.getById(id).map(product => {
      val deleteForm = ProductForms.DeleteForm.fill(DeleteProductData(product.id, product.name))
      Ok(views.html.products.productDelete(deleteForm))
    })
  }

  def deleteFormHandle(): Action[AnyContent] = Action.async { implicit request =>
    ProductForms.DeleteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.products.productDelete(errorForm)))
      },
      productData => {
        productRepository.delete(productData.id).map { _ =>
          Redirect(routes.ProductsController.getByCategoryIdForm()).flashing("success" -> "Product deleted")
        }
      }
    )
  }
}
