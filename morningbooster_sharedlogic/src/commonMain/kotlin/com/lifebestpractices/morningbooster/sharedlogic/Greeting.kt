package com.lifebestpractices.morningbooster.sharedlogic

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}