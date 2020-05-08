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
        if(LocalUser.connected) {
            errorCallback()
            return
        }
        val usrEmail = emails.withIndex().filter {(_, str) -> str == email}
        if(usrEmail.isEmpty()) {
            LocalUser.pseudo = ""
            LocalUser.email = ""
            LocalUser.uid = ""
            LocalUser.connected = false
            errorCallback()
            return
        }
        val (index, foundEmail) = usrEmail[0]
        val pswrd = passwords[index]

        if (password == pswrd){
            LocalUser.email = foundEmail
            LocalUser.pseudo = pseudos[index]
            LocalUser.uid = index.toString()
            LocalUser.connected = true
            successCallback()
        } else {
            LocalUser.pseudo = ""
            LocalUser.email = ""
            LocalUser.uid = ""
            LocalUser.connected = false
            errorCallback()
        }
    }

    override fun disconnect() {
        LocalUser.connected = false
    }

    override fun modify(pseudo: String?, profilePic: Bitmap?, successCallback: () -> Unit, errorCallback: () -> Unit) {
        if(pseudo != null)
            LocalUser.pseudo = pseudo
        if(profilePic != null)
            LocalUser.profilePic = profilePic
        if(pseudo == "REQUESTING_ERROR")
            errorCallback()
        else
            successCallback()
    }

    override fun register(email: String, password: String, pseudo: String, successCallback: () -> Unit, errorCallback: () -> Unit) {
        if(emails.any {str -> str == email}) {
            errorCallback()
            return
        }
        emails.add(email)
        passwords.add(password)
        pseudos.add(pseudo)
        LocalUser.email = email
        LocalUser.pseudo = pseudo
        LocalUser.uid = (emails.size - 1).toString()
        LocalUser.connected = true
        successCallback()
    }

}