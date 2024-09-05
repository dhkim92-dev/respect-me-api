package kr.respectme.group.domain.notifications

enum class DayOfWeek(val value: Int) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7)
    ;
    companion object {
        fun from(value: Int): DayOfWeek {
            return when(value) {
                1 -> MONDAY
                2 -> TUESDAY
                3 -> WEDNESDAY
                4 -> THURSDAY
                5 -> FRIDAY
                6 -> SATURDAY
                7 -> SUNDAY
                else -> throw IllegalArgumentException("Invalid day of week value")
            }
        }

        fun toBits(dayOfWeeks: List<DayOfWeek>): Int {
            return dayOfWeeks.fold(0) { acc, dayOfWeek -> acc or (1 shl (dayOfWeek.value - 1)) }
        }

        fun toList(dayOfWeeks: Int): List<DayOfWeek> {
            return DayOfWeek.values().filter { dayOfWeek -> (dayOfWeek.value - 1) and dayOfWeeks != 0 }
                .sortedBy { dayOfWeek -> dayOfWeek.value }
        }
    }
}