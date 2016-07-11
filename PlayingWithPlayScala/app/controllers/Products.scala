package controllers

import models.Product
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import play.api.mvc.{Flash, Action, Controller}


/**
  * Authors: Hilton et al., 2013
  * Re-Created by bpupadhyaya on 7/9/16.
  */
object Products extends Controller {

  /**
    *
    * @param ean the EAN to check
    * @return true if teh checksum is correct, false otherwise
    */
  private def eanCheck(ean: Long) = {
    def sumDigits(digits: IndexedSeq[(Char,Int)]): Int = {
      digits.map{ _._1}.map { _.toInt}.sum
    }

    val (singles, triples) = ean.toString.reverse.zipWithIndex.partition {
      _._2 % 2 == 0
    }

    (sumDigits(singles) + sumDigits(triples) * 3) % 10 == 0
  }

  private def makeProductForm(error: String, constraint: (Long) => Boolean) = Form(
    mapping (
      "ean" ->  longNumber.verifying("validation.ean.checksum", eanCheck _).verifying(
        error, constraint),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    ) (Product.apply) (Product.unapply)
  )

  private def isUniqueEan(ean: Long): Boolean = Product.findByEan(ean).isEmpty

  private val productForm = makeProductForm("validation.ean.duplicate", isUniqueEan(_))

  private def updateProductForm(ean: Long) =
    makeProductForm("validation.ean.duplicate", {newEan =>
      newEan == ean || isUniqueEan(newEan)
    })

  def list = Action { implicit request =>
    Ok(views.html.products.list(Product.findAll))
  }

  def newProduct = Action { implicit request =>
    val form = if(flash.get("error").isDefined) {
      val errorForm = productForm.bind(flash.data)

      errorForm
    } else
    productForm
    Ok(views.html.products.editProduct(form))
  }

  def show(ean: Long) = Action { implicit request =>
    Product.findByEan(ean).map { product =>
      Ok(views.html.products.details(product))
    }.getOrElse(NotFound)
  }

  def save = Action { implicit request =>
    val newProductForm = productForm.bindFromRequest()

    newProductForm.fold(
      hasErrors = { form =>
        Redirect(routes.Products.newProduct()).flashing(Flash(form.data) +
          ("error" -> Messages("validation.errors")))
      },

      success = { newProduct =>
        Product.add(newProduct)
        val successMessage = ("success" -> Messages("products.new.success", newProduct.name))
        Redirect(routes.Products.show(newProduct.ean)).flashing(successMessage)
      }
    )
  }

  def edit(ean: Long) = Action { implicit request =>
    val form = if (flash.get("error").isDefined)
        updateProductForm(ean).bind(flash.data)
    else
        updateProductForm(ean).fill(Product.findByEan(ean).get)

    Ok(views.html.products.editProduct(form, Some(ean)))
  }

  def update(ean: Long) = Action { implicit request =>
    if(Product.findByEan(ean).isEmpty)
      NotFound
    else {
      val updatedProductForm = updateProductForm(ean).bindFromRequest()

      updatedProductForm.fold(
        hasErrors = { form =>
          Redirect(routes.Products.edit(ean)).flashing(Flash(form.data) +
            ("error" -> Messages("validation.errors")))
        },
        success = { updatedProduct =>
          Product.remove(Product.findByEan(ean).get)
          Product.add(updatedProduct)
          val successMessage = "success" -> Messages("products.update.success", updatedProduct.name)
          Redirect(routes.Products.show(updatedProduct.ean)).flashing(successMessage)
        }
      )
    }
  }
}
