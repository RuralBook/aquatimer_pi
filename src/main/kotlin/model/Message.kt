import kotlinx.serialization.json.*
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val actionName: String,
    var payload: JsonObject,
    val key: String,
)