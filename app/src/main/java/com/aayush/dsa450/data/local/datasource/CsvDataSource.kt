package com.aayush.dsa450.data.local.datasource

import android.content.Context
import com.aayush.dsa450.R
import com.aayush.dsa450.domain.model.DSAProblem
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    fun parseProblemsFromCsv(): List<DSAProblem> {
        return try {
            val inputStream = context.resources.openRawResource(R.raw.fin)
            val csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .parse(inputStream.bufferedReader())
            
            val problems = mutableListOf<DSAProblem>()
            var id = 0
            
            csvParser.forEach { record ->
                try {
                    val type = record.get("Type")
                    val problemName = record.get("Problem")
                    val done = record.get("Done")
                    val url = record.get("Url") ?: ""
                    
                    val difficulty = when {
                        problemName.contains("V.V.V.V.V") -> DSAProblem.Difficulty.HARD
                        problemName.contains("V.IMP") || problemName.contains("IMP") -> DSAProblem.Difficulty.MEDIUM
                        else -> DSAProblem.Difficulty.EASY
                    }
                    
                    problems.add(
                        DSAProblem(
                            id = id++,
                            problemType = type,
                            problemName = problemName,
                            problemUrl = url,
                            isDone = done.uppercase() == "YES",
                            difficulty = difficulty
                        )
                    )
                } catch (e: Exception) {
                    Timber.e(e, "Error parsing CSV record: $record")
                }
            }
            
            csvParser.close()
            inputStream.close()
            
            Timber.d("Successfully parsed ${problems.size} problems from CSV")
            problems
        } catch (e: Exception) {
            Timber.e(e, "Error reading CSV file")
            emptyList()
        }
    }
}
