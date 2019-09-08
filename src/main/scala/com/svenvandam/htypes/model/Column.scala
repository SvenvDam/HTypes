package com.svenvandam.htypes.model

case class Column(family: String, qualifier: String) {
  def getFamilyB: Array[Byte] = family.getBytes

  def getQualifierB: Array[Byte] = qualifier.getBytes
}
