package ru.bilenkod.login3in1testjava.login

data class AuthState(var isLoggedIn: Boolean = false,
                     var userName: String? = null,
                     var userAvatarUrl: String? = null)