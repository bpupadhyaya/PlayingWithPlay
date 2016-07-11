package models

/**
  * Authors: Hilton et al., 2013
  * Re-Created by bpupadhyaya on 7/10/16.
  */
case class Product(ean: Long, name: String, description: String) {
  override def toString = "%s - %s".format(ean,name)
}

object Product {
  var products = Set(
    Product(5010255079763L, "Paperclips Large", "Large Plain Pack of 1000"),
    Product(5018206244666L, "Giant Paperclips", "Giant Plain 51mm 100 pack"),
    Product(5018306332812L, "Paperclip Giant Plain", "Giant Plain Pack of 10000"),
    Product(5018306312913L, "No Tear Paper Clip", "No Tear Extra Large Pack of 1000"),
    Product(5018206244611L, "Zebra Paperclips", "Zebra Length 28mm Assorted 150 Pack")
  )

  def findAll = products.toList.sortBy(_.ean)

  def findByEan(ean: Long) = products.find(_.ean == ean)

  def findByName(query: String) = products.filter(_.name.contains(query))

  def remove(product: Product) = {
    val oldProducts = products
    products = products - product
    oldProducts.contains(product)
  }

  def add(product: Product): Unit = {
    products = products + product
  }
}
