#!/usr/bin/env groovyclient -Cenv DEBUG -cp ../build/classes/main

import org.jggug.kobo.gastah.*

def hoge = "DSFSDKFJSKDLFJSKDLJF"
foo = "FFFFFFFFFFFFFFF"

@IfDef("DEBUG")
xx={
    println ">"*50
    println hoge
    println foo
    hoge *= 5
    foo *= 5
    println "<"*50
}()

println hoge
println foo
