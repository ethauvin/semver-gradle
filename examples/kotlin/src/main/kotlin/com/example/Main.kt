package com.example

import java.io.File

fun main(args: Array<String>) {
    if (args.size == 1) {
        File(args[0]).apply {
            if (exists()) {
                println("> cat $name")
                forEachLine {
                    println(it)
                }
            }
        }
    }
}