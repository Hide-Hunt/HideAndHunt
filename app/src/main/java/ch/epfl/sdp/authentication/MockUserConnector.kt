package ch.epfl.sdp.authentication

import android.graphics.Bitmap

class MockUserConnector : IUserConnector {
    private val pseudos = ArrayList<String>()
    private val emails = ArrayList<String>()
    private val passwords = ArrayList<String>()

    init {
        for(i in 0..5) {
            emails.add("test$i@test.com")
            passwords.add("password$i")
            pseudos.add("Test$i")
        }
    }

    override fun connect(email: String, password: String, successCallback: () -> Unit, errorCallback: () -> Unit) {
        if(User.connected)
            return
        val usrEmail = emails.withIndex().filter {(_, str) -> str == email}
        if(usrEmail.isEmpty()) {
            User.pseudo = ""
            User.email = ""
            User.uid = ""
            User.connected = false
        }
        val (index, foundEmail) = usrEmail[0]
        val pswrd = passwords[index]

        if (password == pswrd){
            User.email = foundEmail
            User.pseudo = pseudos[index]
            User.uid = index.toString()
            User.connected = true
        } else {
            User.pseudo = ""
            User.email = ""
            User.uid = ""
            User.connected = false
        }
    }

    override fun disconnect() {
        User.connected = false
    }

    override fun modify(pseudo: String?, profilePic: Bitmap?, successCallback: () -> Unit, errorCallback: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun register(email: String, password: String, pseudo: String, successCallback: () -> Unit, errorCallback: () -> Unit) {
        if(emails.any {str -> str == email})
            return
        emails.add(email)
        passwords.add(password)
        pseudos.add(pseudo)
        User.email = email
        User.pseudo = pseudo
        User.uid = (emails.size - 1).toString()
        User.connected = true
    }

}