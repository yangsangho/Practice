package kr.yangbob.unitanduitest

open class ParentClass{
    open fun getDescription() = "i'm parent"
    fun getFamilyTest() = "do your best"
}

class ChildClass : ParentClass() {
    override fun getDescription(): String = "i'm child"
}