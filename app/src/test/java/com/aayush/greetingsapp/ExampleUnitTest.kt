package com.aayush.greetingsapp

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.Test

import org.junit.Assert.*
import java.io.InputStream

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `when file is read then content is correct`() {
        // Prepend the path with /raw/
        val inputStream: InputStream? = this::class.java.getResourceAsStream("com.aayush.greetingsapp/raw/fin.xlsx")

        // Check if inputStream is not null
        assertNotNull("Input stream is null", inputStream)

        // Use try-with-resources to automatically close the inputStream
        try {
            val workbook = WorkbookFactory.create(inputStream)
            val workSheet = workbook.getSheetAt(0)
            assertEquals("Reverse the array", workSheet.getRow(5).getCell(1).stringCellValue)
        } catch (e: Exception) {
            // Handle exceptions, print error message, etc.
            e.printStackTrace()
        }
    }
}