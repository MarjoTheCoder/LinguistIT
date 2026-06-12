import com.example.linguistit.model.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.linguistit.model.User

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun registerUser(user: User, passwordTemp: String, onResult: (AuthResult) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, passwordTemp)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid

                    if (uid != null) {
                        // 3. Guardamos los datos extendidos en la colección "users" usando ese UID
                        firestore.collection("users")
                            .document(uid)
                            .set(user) // Firestore puede mapear directamente tu Data Class "User"
                            .addOnSuccessListener {
                                onResult(AuthResult.Success)
                            }
                            .addOnFailureListener { e ->
                                onResult(AuthResult.Error(e.message ?: "Error al guardar en Firestore"))
                            }
                    }
                } else {
                    onResult(AuthResult.Error(task.exception?.message ?: "Error en autenticación"))
                }
            }
    }

    fun getCurrentUserProfile(onResult: (User?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userProfile = document.toObject(User::class.java)
                        onResult(userProfile)
                    } else {
                        onResult(null)
                    }
                }
                .addOnFailureListener {
                    onResult(null)
                }
        } else {
            onResult(null)
        }
    }

    fun login(email: String, pass: String, onResult: (AuthResult) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(AuthResult.Success)
                } else {
                    onResult(AuthResult.Error(task.exception?.message ?: "Error al iniciar sesión"))
                }
            }
    }

    fun sendPasswordReset(email: String, onResult: (AuthResult) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(AuthResult.Success)
                } else {
                    onResult(AuthResult.Error(task.exception?.message ?: "Error al enviar el correo de recuperación"))
                }
            }
    }

    fun updateUserProfile(user: User, onResult: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            firestore.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener {
                    onResult(true)
                }
                .addOnFailureListener {
                    onResult(false)
                }
        } else {
            onResult(false)
        }
    }
}