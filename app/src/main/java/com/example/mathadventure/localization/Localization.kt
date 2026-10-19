package com.example.mathadventure.localization

import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(val code: String, val displayName: String, val layoutDirection: LayoutDirection) {
    ENGLISH("en", "English", LayoutDirection.Ltr),
    ARABIC("ar", "العربية", LayoutDirection.Rtl)
}

enum class NumeralFormat(val displayNameEn: String, val displayNameAr: String) {
    WESTERN("Western (0-9)", "الأرقام الإنجليزية (0-9)"),
    ARABIC_INDIC("Eastern (٠-٩)", "الأرقام العربية المشرقية (٠-٩)")
}

object MathFormatter {
    private val easternDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')

    fun format(number: Int, format: NumeralFormat): String {
        return format(number.toString(), format)
    }

    fun format(text: String, format: NumeralFormat): String {
        if (format == NumeralFormat.WESTERN) return text
        val sb = StringBuilder()
        for (ch in text) {
            if (ch in '0'..'9') {
                sb.append(easternDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun formatEquation(a: Int, op: String, b: Int, ans: Int?, format: NumeralFormat): String {
        val aStr = format(a, format)
        val bStr = format(b, format)
        val ansStr = ans?.let { format(it, format) } ?: "?"
        return "$aStr $op $bStr = $ansStr"
    }
}

class AppStrings(val language: AppLanguage) {
    val isAr: Boolean = language == AppLanguage.ARABIC

    // Navigation & Common
    val appTitle: String = if (isAr) "مغامرات الرياضيات" else "Math Adventure"
    val home: String = if (isAr) "الرئيسية" else "Home"
    val multiplyAcademy: String = if (isAr) "أكاديمية الضرب" else "Multiplication"
    val divideAcademy: String = if (isAr) "أكاديمية القسمة" else "Division"
    val mentalLab: String = if (isAr) "معمل الحساب الذهني" else "Mental Math"
    val gameArcade: String = if (isAr) "ألعاب الرياضيات" else "Games Arcade"
    val testsAndQuizzes: String = if (isAr) "الاختبارات والتحديات" else "Tests & Quizzes"
    val rewards: String = if (isAr) "الجوائز والأوسمة" else "Rewards"
    val parentZone: String = if (isAr) "منطقة الوالدين" else "Parent Zone"
    val settings: String = if (isAr) "الإعدادات" else "Settings"
    val back: String = if (isAr) "رجوع" else "Back"
    val next: String = if (isAr) "التالي" else "Next"
    val checkAnswer: String = if (isAr) "تحقق من الإجابة" else "Check Answer"
    val tryAgain: String = if (isAr) "حاول مرة أخرى" else "Try Again"
    val correct: String = if (isAr) "رائع! إجابة صحيحة!" else "Awesome! Correct!"
    val niceTry: String = if (isAr) "محاولة جيدة! لنتعلم معًا:" else "Nice try! Let's learn together:"
    val stars: String = if (isAr) "نجوم" else "Stars"
    val coins: String = if (isAr) "نقود" else "Coins"
    val streak: String = if (isAr) "أيام مستمرة" else "Day Streak"
    val startAdventure: String = if (isAr) "ابدأ المغامرة" else "Start Adventure"

    // Guides & Mascot
    val guideName: String = if (isAr) "الروبوت كوانتم" else "Robo-Chip"
    val guideWelcome: String = if (isAr) "أهلاً بك يا بطل! أنا الروبوت كوانتم، مستعد لرحلة اكتشاف أسرار الرياضيات في المعمل الخارق؟" 
        else "Welcome Cadet! I'm Robo-Chip. Ready to explore the high-tech math lab and master multiplication & division?"
    val companionPanda: String = if (isAr) "الباندا بامبو" else "Bamboo Panda"
    val companionFox: String = if (isAr) "الثعلب ريكس" else "Rex Fox"
    val companionOwl: String = if (isAr) "البومة حكيمة" else "Hakeem Owl"

    // Methods - Multiplication
    val multMethod1Title: String = if (isAr) "المجموعات المتساوية والمصفوفات" else "Equal Groups & Visual Arrays"
    val multMethod1Desc: String = if (isAr) "شاهد كيف تتجمع الأشياء في صفوف وأعمدة متساوية لسهولة العد" else "See how objects group into equal rows and columns"
    val multMethod2Title: String = if (isAr) "الجمع المتكرر" else "Repeated Addition"
    val multMethod2Desc: String = if (isAr) "الضرب هو جمع نفس العدد عدة مرات بسرعة وسهولة" else "Multiplication is just adding the same number multiple times"
    val multMethod3Title: String = if (isAr) "قفزات خط الأعداد" else "Number Line Jumps"
    val multMethod3Desc: String = if (isAr) "اقفز قفزات متساوية للأمام لتصل إلى الناتج بكل وضوح" else "Hop forward in equal leaps to land on the product"
    val multMethod4Title: String = if (isAr) "جداول الضرب (٠ إلى ١٢)" else "Multiplication Tables (0 to 12)"
    val multMethod4Desc: String = if (isAr) "استكشف كل جدول تدريجيًا مع أنماط الأعداد السحرية" else "Explore tables one by one with visual number patterns"
    val multMethod5Title: String = if (isAr) "خاصية الإبدال" else "Commutative Property"
    val multMethod5Desc: String = if (isAr) "لماذا ٣ × ٤ = ٤ × ٣؟ لأن المصفوفة تحتفظ بعددها عند تدويرها!" else "Why 3 × 4 = 4 × 3? Rotating an array keeps the total the same!"
    val multMethod6Title: String = if (isAr) "خاصية التوزيع" else "Distributive Property"
    val multMethod6Desc: String = if (isAr) "قسّم الأعداد الصعبة إلى جزأين سهلين: ٧ × ٨ = (٧ × ٥) + (٧ × ٣)" else "Break hard numbers into easy parts: 7 × 8 = (7 × 5) + (7 × 3)"
    val multMethod7Title: String = if (isAr) "استراتيجية المضاعفة والتنصيف" else "Doubling & Halving"
    val multMethod7Desc: String = if (isAr) "ضاعف عددًا ونصّف الآخر: ٤ × ٨ = ٢ × ١٦ = ٣٢" else "Double one and halve the other: 4 × 8 = 2 × 16 = 32"
    val multMethod8Title: String = if (isAr) "الأعداد الصديقة والتعويض" else "Friendly Numbers & Compensation"
    val multMethod8Desc: String = if (isAr) "احسب ٩ × ٧ كـ (١٠ × ٧) − ٧ = ٦٣ بكل بساطة" else "Calculate 9 × 7 as (10 × 7) − 7 = 63 in a flash"
    val multMethod9Title: String = if (isAr) "حيل الضرب في ١٠ و ١٠٠ والأصفار" else "Powers of 10 & Zero Tricks"
    val multMethod9Desc: String = if (isAr) "عند الضرب في ١٠، حرك المنازل وأضف صفرًا إلى اليمين" else "When multiplying by 10 or 100, shift places and add zeroes"

    // Methods - Division
    val divMethod1Title: String = if (isAr) "التوزيع العادل بالتساوي" else "Equal Sharing"
    val divMethod1Desc: String = if (isAr) "وزّع ١٢ قطعة بالتساوي بين ٣ أصدقاء ليعرف كل واحد نصيبه" else "Distribute 12 items equally among 3 friends"
    val divMethod2Title: String = if (isAr) "التجميع في مجموعات متساوية" else "Equal Grouping"
    val divMethod2Desc: String = if (isAr) "كم مجموعة من ٣ يمكننا تكوينها من ١٢ قطعة؟" else "How many groups of 3 can be formed from 12 items?"
    val divMethod3Title: String = if (isAr) "الطرح المتكرر" else "Repeated Subtraction"
    val divMethod3Desc: String = if (isAr) "اطرح المقسوم عليه مرارًا حتى تصل إلى الصفر: ١٢ − ٣ − ٣ − ٣ − ٣ = ٠" else "Subtract the divisor repeatedly until reaching zero: 12 − 3 − 3 − 3 − 3 = 0"
    val divMethod4Title: String = if (isAr) "القفزات العكسية على خط الأعداد" else "Backward Number Line Jumps"
    val divMethod4Desc: String = if (isAr) "ابدأ من العدد واقْفز إلى الخلف بقفزات متساوية حتى الصفر" else "Start from the total and hop backward in equal steps"
    val divMethod5Title: String = if (isAr) "عائلات الحقائق المترابطة" else "Fact Family Relationships"
    val divMethod5Desc: String = if (isAr) "٣ × ٤ = ١٢ تعني مباشرة أن ١٢ ÷ ٣ = ٤ و ١٢ ÷ ٤ = ٣!" else "3 × 4 = 12 connects directly to 12 ÷ 3 = 4 and 12 ÷ 4 = 3!"
    val divMethod6Title: String = if (isAr) "القسمة كبحث عن العامل المفقود" else "Missing Factor Division"
    val divMethod6Desc: String = if (isAr) "اسأل نفسك: ما هو العدد الذي إذا ضربته في ٣ كان الناتج ١٢؟" else "Ask: What number multiplied by 3 gives 12?"
    val divMethod7Title: String = if (isAr) "معمل القسمة المطولة خطوة بخطوة" else "Step-by-Step Long Division"
    val divMethod7Desc: String = if (isAr) "تعلم الخوارزمية القياسية: قسّم، اضرب، اطرح، أنزل الرقم التالي" else "Learn the standard algorithm: Divide, Multiply, Subtract, Bring down"
    val divMethod8Title: String = if (isAr) "القسمة مع الباقي في الحياة الواقعية" else "Division with Remainders"
    val divMethod8Desc: String = if (isAr) "ماذا يحدث عندما لا تنقسم الأشياء بالتساوي تمامًا؟ نفهم معنى الباقي" else "What happens when items cannot share evenly? Discover the remainder"
    val divMethod9Title: String = if (isAr) "القسمة الذهنية وقواعد قابلية القسمة" else "Mental Division & Divisibility"
    val divMethod9Desc: String = if (isAr) "اكتشف متى يقبل العدد القسمة على ٢، ٣، ٥، و ١٠ بلمح البصر" else "Spot numbers divisible by 2, 3, 5, and 10 instantly"
    val divMethod10Title: String = if (isAr) "التحقق من صحة القسمة بالضرب" else "Checking Division by Multiplication"
    val divMethod10Desc: String = if (isAr) "المقسوم = (المقسوم عليه × الناتج) + الباقي" else "Dividend = (Divisor × Quotient) + Remainder"

    // Games
    val game1Title: String = if (isAr) "صاروخ الرياضيات" else "Rocket Math"
    val game1Desc: String = if (isAr) "أجب على المسائل لتزويد خزانات الصاروخ بالوقود والانطلاق إلى الفضاء!" else "Fuel the thrusters with correct answers to blast into space!"
    val game2Title: String = if (isAr) "متجر فواكه الباندا" else "Panda's Fruit Shop"
    val game2Desc: String = if (isAr) "احسب الأسعار ووزع الفواكه في أكياس متساوية للزبائن" else "Calculate total prices and pack equal fruit boxes for customers"
    val game3Title: String = if (isAr) "صائد الكنوز الرقمية" else "Number Treasure Hunt"
    val game3Desc: String = if (isAr) "حل معادلات الأرقام لفتح أقفال صناديق الذهب والجواهر" else "Crack math codes to open multi-locked treasure chests"
    val game4Title: String = if (isAr) "معركة الوحوش اللطيفة" else "Monster Math Battle"
    val game4Desc: String = if (isAr) "أطلق شعاع الطاقة التعليمي لترويض الوحوش الظريفة وكسب صداقتها!" else "Beam math power to tame friendly cartoon monsters!"
    val game5Title: String = if (isAr) "بينجو الضرب السحري" else "Multiplication Bingo"
    val game5Desc: String = if (isAr) "طابق نواتج الضرب لتكمل خطًا كاملاً في لوحة البينجو" else "Match products to complete rows on your bingo grid"
    val game6Title: String = if (isAr) "مطابقة عائلات الأعداد" else "Fact Family Link"
    val game6Desc: String = if (isAr) "اربط كل معادلة ضرب بمعادلة القسمة التوأم لها" else "Match each multiplication fact with its division twin"
    val game7Title: String = if (isAr) "المحقق والأرقام المفقودة" else "Missing Number Detective"
    val game7Desc: String = if (isAr) "ابحث عن العامل الناقص أو الناتج المختفي بالعدسة المكبرة" else "Find the missing factor or product with your detective lens"
    val game8Title: String = if (isAr) "بناء جسر الأصدقاء" else "Build the Bridge"
    val game8Desc: String = if (isAr) "ضع ألواح الجسر المرقمة بالترتيب الصحيح ليعبر الأصدقاء الوادي" else "Place numbered bridge planks to help friends cross safely"
    val game9Title: String = if (isAr) "التحدي اليومي السريع" else "Daily Math Challenge"
    val game9Desc: String = if (isAr) "٥ مسائل منوعة يوميًا لتحافظ على سلسلة تفوقك وتكسب الجواهر" else "5 quick mixed questions daily to build your streak and earn gems"
    val game10Title: String = if (isAr) "تحدي الزعيم كوانتم الخارق" else "Quantum Boss Challenge"
    val game10Desc: String = if (isAr) "تحدي من ٣ مراحل للمحترفين لفتح درع الروبوت الذهبي الأسطوري!" else "A 3-stage challenge for champions to unlock the Golden Robo-Suit!"

    // Assessment & Parent Zone
    val diagnosticTitle: String = if (isAr) "اختبار تحديد المستوى الذكي" else "Adaptive Diagnostic Placement"
    val diagnosticDesc: String = if (isAr) "أسئلة لطيفة سريعة تحدد المستوى المناسب لك بدون أي توتر" else "Fun low-stress questions to match your current math superpower"
    val reviewMistakes: String = if (isAr) "مراجعة الأخطاء وتثبيت الفهم" else "Review Mistakes & Practice"
    val parentPinPrompt: String = if (isAr) "بوابة الوالدين: يرجى حل المسألة للدخول" else "Parent Gate: Solve to enter"
    val timeSpentLearning: String = if (isAr) "الوقت المستغرق في التعلم" else "Learning Time"
    val accuracyByTable: String = if (isAr) "مستوى الإتقان حسب جدول الضرب" else "Accuracy by Times-Table"
    val resetProgress: String = if (isAr) "إعادة ضبط التقدم (تحذير)" else "Reset All Progress (Warning)"
    val resetConfirm: String = if (isAr) "هل أنت متأكد من مسح جميع بيانات التعلم والنجوم؟" else "Are you sure you want to reset all stars, coins and history?"
    val cancel: String = if (isAr) "إلغاء" else "Cancel"
    val confirm: String = if (isAr) "تأكيد" else "Confirm"
    val scoreReport: String = if (isAr) "تقرير النتيجة" else "Score Report"
    val masteryReached: String = if (isAr) "مستوى الإتقان: تم الإتقان بنجاح (أكثر من ٨٥٪)!" else "Mastery: Reached (85%+)"
    val keepPracticing: String = if (isAr) "تحتاج لمزيد من التدريب لتثبيت المفهوم" else "Needs more practice to solidify concept"
    val soundEffects: String = if (isAr) "المؤثرات الصوتية" else "Sound Effects"
    val voiceSpeech: String = if (isAr) "القراءة الصوتية التلقائية" else "Text-to-Speech Voice"
    val numeralStyle: String = if (isAr) "نمط كتابة الأرقام" else "Numeral Style"
    val minutesUnit: String = if (isAr) "دقيقة" else "minutes"
}
