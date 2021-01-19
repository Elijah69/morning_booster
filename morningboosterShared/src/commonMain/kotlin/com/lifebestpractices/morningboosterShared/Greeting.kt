package com.lifebestpractices.morningboosterShared

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}