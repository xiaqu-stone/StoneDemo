package com.stone.templateapp

/**
 * Created By: sqq
 * Created Time: 2019-04-26 11:24.
 */
object TestObject {

    var property = ""
        get() {
            println("the get is going: ")
//            Logs.d("")
            if (field.isEmpty()) {
                println("the field is Empty in get")
//                Logs.d("the field is Empty in get")
//                field = "field is valid in get()"
                property = "field is valid in get()"
            } else {
//                Logs.d("the filed is not Empty in get ")
                println("the filed is not Empty in get")
            }
            return field
        }
        set(value) {
//            Logs.d("the set is going ")
            println("the set is going ")
            field = value
        }
}