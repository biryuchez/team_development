package pro.fateeva.pillsreminder.ui.screens.onceperday

class TimeMapper {
    fun getTimeFromTimePicker(hour: Int, minute: Int): String {
        val timeParams = listOf(hour.toString(), minute.toString())
        val result = StringBuilder()
        for (i in timeParams.indices) {
            if (timeParams[i].length < 2) {
                result.append("0")
            }
            result.append(timeParams[i])
            if (i == 0) {
                result.append(":")
            }
        }
        return result.toString()
    }
}