package com.nexters.misik.data.model

data class OcrParsedResponse(
    val parsed: List<OcrParsedItem>,
) {
    companion object {
        fun createMock(): OcrParsedResponse {
            return OcrParsedResponse(
                parsed = listOf(
                    OcrParsedItem("품명", "카야토스트+음료세트"),
                    OcrParsedItem("품명", "아메리카노"),
                    OcrParsedItem("품명", "샌드위치"),
                ),
            )
        }

        fun createMockStr(): String {
            return """
                {
                    "parsed": [
                        {
                            "key": "품명",
                            "value": "카야토스트+음료세트"
                        },
                        {
                            "key": "품명",
                            "value": "아메리카노"
                        },
                        {
                            "key": "품명",
                            "value": "샌드위치"
                        }
                    ]
                }
            """.trimIndent()
        }
    }
}

data class OcrParsedItem(
    val key: String,
    val value: String,
)
