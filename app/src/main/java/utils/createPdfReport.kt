import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream

fun createPdfReport(request: InventoryRequest) {
    // Создаем новый документ
    val pdfDocument = PdfDocument()

    // Указываем размер страницы (A4)
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

    // Начинаем страницу
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Настраиваем шрифт и размер текста
    val paint = Paint()
    paint.textSize = 14f
    paint.isFakeBoldText = true

    // Пишем информацию в PDF
    canvas.drawText("Отчет по инвентаризации", 100f, 100f, paint)
    paint.textSize = 12f
    paint.isFakeBoldText = false

    canvas.drawText("Заявка №${request.id}", 100f, 130f, paint)
    canvas.drawText("Сотрудник: ${request.employee}", 100f, 160f, paint)
    canvas.drawText("Дедлайн: ${request.deadline}", 100f, 190f, paint)
    canvas.drawText("Статус: ${request.status}", 100f, 220f, paint)

    // Добавляем склады
    canvas.drawText("Склады:", 100f, 250f, paint)
    var yPosition = 280f
    for (warehouse in request.warehouses) {
        canvas.drawText("- ${warehouse}", 120f, yPosition, paint)
        yPosition += 30f
    }

    // Завершаем страницу
    pdfDocument.finishPage(page)

    // Создаем путь для сохранения PDF
    val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
    val filePath = "$directoryPath/inventory_report_${request.id}.pdf"
    val file = File(filePath)

    try {
        // Сохраняем PDF файл
        val outputStream = FileOutputStream(file)
        pdfDocument.writeTo(outputStream)
        outputStream.close()

        Log.d("PDFReport", "PDF отчет успешно создан: $filePath")

    } catch (e: Exception) {
        Log.e("PDFReport", "Ошибка создания PDF: ${e.message}")
    } finally {
        // Закрываем документ
        pdfDocument.close()
    }
}
