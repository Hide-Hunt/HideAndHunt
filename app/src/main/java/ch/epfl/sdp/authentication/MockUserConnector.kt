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
        if(User.connected)
            return false
        val usrEmail = emails.withIndex().filter {(_, str) -> str == email}
        if(usrEmail.isEmpty()) {
            User.username = ""
            User.uid = ""
            User.connected = false
            return false
        }
        val (index, foundEmail) = usrEmail[0]
        val pswrd = passwords[index]

        if (password == pswrd){
            User.username = foundEmail
            User.uid = index.toString()
            User.connected = true
        } else {
            User.username = ""
            User.uid = ""
            User.connected = false
        }

        return User.connected
    }

    override fun disconnect(): Boolean {
        User.connected = false
        return true
    }

    override fun register(email: String, password: String): Boolean {
        if(emails.any {str -> str == email})
            return false
        emails.add(email)
        passwords.add(password)
        User.username = email
        User.uid = (emails.size - 1).toString()
        User.connected = true
        return true
    }

}