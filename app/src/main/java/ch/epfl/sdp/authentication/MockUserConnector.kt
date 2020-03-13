package ch.epfl.sdp.authentication

class MockUserConnector : IUserConnector {
    private val emails = ArrayList<String>()
    private val passwords = ArrayList<String>()

    init {
        for(i in 0..5) {
            emails.add("test$i@test.com")
            passwords.add("password$i")
        }
    }

    override fun connect(email: String, password: String): Boolean {
        if(user.connected)
            return false
        val usrEmail = emails.withIndex().filter {(i, str) -> str == email}
        if(usrEmail.isEmpty()) {
            user.username = ""
            user.uid = ""
            user.connected = false
            return false
        }
        val (index, foundEmail) = usrEmail[0]
        val pswrd = passwords[index]

        if (password == pswrd){
            user.username = foundEmail
            user.uid = index.toString()
            user.connected = true
        } else {
            user.username = ""
            user.uid = ""
            user.connected = false
        }

        return user.connected
    }

    override fun disconnect(): Boolean {
        user.connected = false
        return true
    }

    override fun register(email: String, password: String): Boolean {
        if(emails.any {str -> str == email})
            return false
        emails.add(email)
        passwords.add(password)
        user.username = email
        user.uid = "0"
        user.connected = true
        return true
    }

}