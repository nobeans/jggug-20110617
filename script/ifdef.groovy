import org.jggug.kobo.gastah.*

@IfDef("HOGE")
a = {
    println "...."
    println "Hooooooooooooooooooooooogeeeee!"
}()

@IfNotDef("HOGE")
b = {
    println "...."
    println "Where is hoge?"
}()

