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
        if(LocalUser.connected)
            return false
        val usrEmail = emails.withIndex().filter {(_, str) -> str == email.toLowerCase()}
        if(usrEmail.isEmpty()) {
            LocalUser.username = ""
            LocalUser.uid = ""
            LocalUser.connected = false
            return false
        }
        val (index, foundEmail) = usrEmail[0]
        val pswrd = passwords[index]

        if (password == pswrd){
            LocalUser.username = foundEmail
            LocalUser.uid = index.toString()
            LocalUser.connected = true
        } else {
            LocalUser.username = ""
            LocalUser.uid = ""
            LocalUser.connected = false
        }

        return LocalUser.connected
    }

    override fun disconnect(): Boolean {
        LocalUser.connected = false
        return true
    }

    override fun register(email: String, password: String): Boolean {
        if(emails.any {str -> str == email.toLowerCase()})
            return false
        emails.add(email)
        passwords.add(password)
        LocalUser.username = email
        LocalUser.uid = (emails.size - 1).toString()
        LocalUser.connected = true
        return true
    }

}