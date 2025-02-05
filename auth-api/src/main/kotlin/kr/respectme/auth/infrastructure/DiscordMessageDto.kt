package kr.respectme.auth.infrastructure

data class Embed(
    val title: String,
    val description: String
)

data class DiscordMessageDto(
    val content: String,
    val embeds : List<Embed> = emptyList()
) {

}