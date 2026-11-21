package com.example.data.static

data class VocabularyWord(
    val english: String,
    val bangla: String,
    val exampleEn: String,
    val exampleBn: String,
    val imageUrl: String
)

enum class TenseCategory(val label: String) {
    PRESENT("Present"),
    PAST("Past"),
    FUTURE("Future")
}

data class TenseExample(
    val english: String,
    val bangla: String
)

data class TenseLesson(
    val name: String,
    val category: TenseCategory,
    val usageBn: String,
    val positive: String,
    val negative: String,
    val question: String,
    val negativeQuestion: String,
    val examples: List<TenseExample>
)

object LearningContent {
    val vocabularyWords: List<VocabularyWord> = listOf(
        VocabularyWord(
            english = "Apple",
            bangla = "আপেল",
            exampleEn = "I eat an apple.",
            exampleBn = "আমি একটি আপেল খাই।",
            imageUrl = "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=300"
        ),
        VocabularyWord(
            english = "Book",
            bangla = "বই",
            exampleEn = "This is my book.",
            exampleBn = "এটি আমার বই।",
            imageUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=300"
        ),
        VocabularyWord(
            english = "Beautiful",
            bangla = "সুন্দর",
            exampleEn = "The flower is beautiful.",
            exampleBn = "ফুলটি সুন্দর।",
            imageUrl = "https://images.unsplash.com/photo-1490750967868-88aa4486c946?w=300"
        ),
        VocabularyWord(
            english = "School",
            bangla = "বিদ্যালয়",
            exampleEn = "I go to school.",
            exampleBn = "আমি স্কুলে যাই।",
            imageUrl = "https://images.unsplash.com/photo-1580582932707-520aed937b7b?w=300"
        ),
        VocabularyWord(
            english = "Water",
            bangla = "পানি",
            exampleEn = "I drink water.",
            exampleBn = "আমি পানি পান করি।",
            imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1cf4d?w=300"
        ),
        VocabularyWord(
            english = "Friend",
            bangla = "বন্ধু",
            exampleEn = "He is my friend.",
            exampleBn = "সে আমার বন্ধু।",
            imageUrl = "https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=300"
        ),
        VocabularyWord(
            english = "Family",
            bangla = "পরিবার",
            exampleEn = "I love my family.",
            exampleBn = "আমি আমার পরিবারকে ভালোবাসি।",
            imageUrl = "https://images.unsplash.com/photo-1511895426328-dc8714191300?w=300"
        ),
        VocabularyWord(
            english = "Flower",
            bangla = "ফুল",
            exampleEn = "This flower is red.",
            exampleBn = "এই ফুলটি লাল।",
            imageUrl = "https://images.unsplash.com/photo-1490750967868-88aa4486c946?w=300"
        ),
        VocabularyWord(
            english = "House",
            bangla = "বাড়ি",
            exampleEn = "My house is big.",
            exampleBn = "আমার বাড়ি বড়।",
            imageUrl = "https://images.unsplash.com/photo-1518780664697-55e3ad937233?w=300"
        ),
        VocabularyWord(
            english = "Happy",
            bangla = "সুখী",
            exampleEn = "I am happy.",
            exampleBn = "আমি সুখী।",
            imageUrl = "https://images.unsplash.com/photo-1499209974431-9dddcece7f88?w=300"
        ),
        VocabularyWord(
            english = "Knowledge",
            bangla = "জ্ঞান",
            exampleEn = "Knowledge is power.",
            exampleBn = "জ্ঞানই শক্তি।",
            imageUrl = "https://images.unsplash.com/photo-1507842217343-583bb7270b66?w=300"
        ),
        VocabularyWord(
            english = "Nature",
            bangla = "প্রকৃতি",
            exampleEn = "I love nature.",
            exampleBn = "আমি প্রকৃতিকে ভালোবাসি।",
            imageUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=300"
        )
    )

    val tenses: List<TenseLesson> = listOf(
        TenseLesson(
            name = "Present Simple",
            category = TenseCategory.PRESENT,
            usageBn = "অভ্যাস, নিয়মিত কাজ ও সাধারণ সত্য বোঝায়।",
            positive = "Subject + V1(s/es) + Object",
            negative = "Subject + do/does + not + V1 + Object",
            question = "Do/Does + Subject + V1 + Object?",
            negativeQuestion = "Do/Does + Subject + not + V1 + Object?",
            examples = listOf(
                TenseExample("I play football.", "আমি ফুটবল খেলি।"),
                TenseExample("She reads a book.", "সে একটি বই পড়ে।")
            )
        ),
        TenseLesson(
            name = "Present Continuous",
            category = TenseCategory.PRESENT,
            usageBn = "বর্তমানে কোনো কাজ চলছে বোঝায়।",
            positive = "Subject + am/is/are + V-ing + Object",
            negative = "Subject + am/is/are + not + V-ing + Object",
            question = "Am/Is/Are + Subject + V-ing + Object?",
            negativeQuestion = "Am/Is/Are + Subject + not + V-ing + Object?",
            examples = listOf(
                TenseExample("I am reading.", "আমি পড়ছি।"),
                TenseExample("They are playing.", "তারা খেলছে।")
            )
        ),
        TenseLesson(
            name = "Present Perfect",
            category = TenseCategory.PRESENT,
            usageBn = "কাজটি সম্পন্ন হয়েছে বোঝায়।",
            positive = "Subject + have/has + V3 + Object",
            negative = "Subject + have/has + not + V3 + Object",
            question = "Have/Has + Subject + V3 + Object?",
            negativeQuestion = "Have/Has + Subject + not + V3 + Object?",
            examples = listOf(
                TenseExample("I have eaten rice.", "আমি ভাত খেয়েছি।"),
                TenseExample("She has finished her work.", "সে তার কাজ শেষ করেছে।")
            )
        ),
        TenseLesson(
            name = "Present Perfect Continuous",
            category = TenseCategory.PRESENT,
            usageBn = "অতীতে শুরু হয়ে এখনো চলছে এমন কাজ বোঝায়।",
            positive = "Subject + have/has been + V-ing",
            negative = "Subject + have/has not been + V-ing",
            question = "Have/Has + Subject + been + V-ing?",
            negativeQuestion = "Have/Has + Subject + not been + V-ing?",
            examples = listOf(
                TenseExample("I have been reading for two hours.", "আমি দুই ঘণ্টা ধরে পড়ছি।"),
                TenseExample("She has been working since morning.", "সে সকাল থেকে কাজ করছে।")
            )
        ),
        TenseLesson(
            name = "Past Simple",
            category = TenseCategory.PAST,
            usageBn = "অতীতে শেষ হওয়া কাজ বোঝায়।",
            positive = "Subject + V2 + Object",
            negative = "Subject + did not + V1 + Object",
            question = "Did + Subject + V1 + Object?",
            negativeQuestion = "Did + Subject + not + V1 + Object?",
            examples = listOf(
                TenseExample("I played football.", "আমি ফুটবল খেলেছিলাম।"),
                TenseExample("He went to school.", "সে স্কুলে গিয়েছিল।")
            )
        ),
        TenseLesson(
            name = "Past Continuous",
            category = TenseCategory.PAST,
            usageBn = "অতীতের কোনো নির্দিষ্ট সময়ে কাজ চলছিল বোঝায়।",
            positive = "Subject + was/were + V-ing",
            negative = "Subject + was/were + not + V-ing",
            question = "Was/Were + Subject + V-ing?",
            negativeQuestion = "Was/Were + Subject + not + V-ing?",
            examples = listOf(
                TenseExample("I was reading.", "আমি পড়ছিলাম।"),
                TenseExample("They were playing.", "তারা খেলছিল।")
            )
        ),
        TenseLesson(
            name = "Past Perfect",
            category = TenseCategory.PAST,
            usageBn = "অতীতের অন্য একটি কাজের আগে কাজ শেষ হয়েছিল বোঝায়।",
            positive = "Subject + had + V3",
            negative = "Subject + had not + V3",
            question = "Had + Subject + V3?",
            negativeQuestion = "Had + Subject + not + V3?",
            examples = listOf(
                TenseExample("I had eaten rice.", "আমি ভাত খেয়েছিলাম।"),
                TenseExample("She had gone home.", "সে বাড়ি গিয়েছিল।")
            )
        ),
        TenseLesson(
            name = "Past Perfect Continuous",
            category = TenseCategory.PAST,
            usageBn = "অতীতে কোনো সময় পর্যন্ত কাজ চলছিল বোঝায়।",
            positive = "Subject + had been + V-ing",
            negative = "Subject + had not been + V-ing",
            question = "Had + Subject + been + V-ing?",
            negativeQuestion = "Had + Subject + not been + V-ing?",
            examples = listOf(
                TenseExample("I had been reading for two hours.", "আমি দুই ঘণ্টা ধরে পড়ছিলাম।"),
                TenseExample("They had been working.", "তারা কাজ করছিল।")
            )
        ),
        TenseLesson(
            name = "Future Simple",
            category = TenseCategory.FUTURE,
            usageBn = "ভবিষ্যতে কোনো কাজ হবে বোঝায়।",
            positive = "Subject + will + V1",
            negative = "Subject + will not + V1",
            question = "Will + Subject + V1?",
            negativeQuestion = "Will + Subject + not + V1?",
            examples = listOf(
                TenseExample("I will go to school.", "আমি স্কুলে যাব।"),
                TenseExample("She will read a book.", "সে একটি বই পড়বে।")
            )
        ),
        TenseLesson(
            name = "Future Continuous",
            category = TenseCategory.FUTURE,
            usageBn = "ভবিষ্যতের কোনো সময়ে কাজ চলতে থাকবে বোঝায়।",
            positive = "Subject + will be + V-ing",
            negative = "Subject + will not be + V-ing",
            question = "Will + Subject + be + V-ing?",
            negativeQuestion = "Will + Subject + not be + V-ing?",
            examples = listOf(
                TenseExample("I will be reading.", "আমি পড়তে থাকব।"),
                TenseExample("They will be playing.", "তারা খেলতে থাকবে।")
            )
        ),
        TenseLesson(
            name = "Future Perfect",
            category = TenseCategory.FUTURE,
            usageBn = "ভবিষ্যতের নির্দিষ্ট সময়ের আগে কাজ শেষ হবে বোঝায়।",
            positive = "Subject + will have + V3",
            negative = "Subject + will not have + V3",
            question = "Will + Subject + have + V3?",
            negativeQuestion = "Will + Subject + not have + V3?",
            examples = listOf(
                TenseExample("I will have finished the work.", "আমি কাজটি শেষ করে ফেলব।"),
                TenseExample("She will have arrived.", "সে পৌঁছে যাবে।")
            )
        ),
        TenseLesson(
            name = "Future Perfect Continuous",
            category = TenseCategory.FUTURE,
            usageBn = "ভবিষ্যতের নির্দিষ্ট সময় পর্যন্ত কাজ চলতে থাকবে বোঝায়।",
            positive = "Subject + will have been + V-ing",
            negative = "Subject + will not have been + V-ing",
            question = "Will + Subject + have been + V-ing?",
            negativeQuestion = "Will + Subject + not have been + V-ing?",
            examples = listOf(
                TenseExample("I will have been reading for two hours.", "আমি দুই ঘণ্টা ধরে পড়তে থাকব।"),
                TenseExample("He will have been working for a year.", "সে এক বছর ধরে কাজ করতে থাকবে।")
            )
        )
    )
}
