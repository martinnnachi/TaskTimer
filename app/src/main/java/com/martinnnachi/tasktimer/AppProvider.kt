package com.martinnnachi.tasktimer

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

/** Provider for the TaskTimer app. This is the only class that knows about [AppDatabase].
 */

private const val TAG = "AppProvider"

const val CONTENT_AUTHORITY = "com.martinnnachi.tasktimer.provider"
private const val TASKS = 100
private const val TASKS_ID = 101

private const val TIMINGS = 200
private const val TIMINGS_ID = 201

private const val TASK_DURATIONS = 400
private const val TASK_DURATIONS_ID = 401

val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

class AppProvider : ContentProvider() {

    private val uriMatcher by lazy { buildUriMatcher() }

    private fun buildUriMatcher(): UriMatcher {
        Log.d(TAG, "buildUriMatcher: starts")
        val matcher = UriMatcher(UriMatcher.NO_MATCH)

        // e.g content://com.martinnnachi.tasktimer.provider/Tasks
        matcher.addURI(CONTENT_AUTHORITY, TasksContract.TABLE_NAME, TASKS)

        // e.g content://com.martinnnachi.tasktimer.provider/Tasks/8
        matcher.addURI(CONTENT_AUTHORITY, "${TasksContract.TABLE_NAME}/#", TASKS_ID)

//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME, TIMINGS)
//        matcher.addURI(CONTENT_AUTHORITY, "${TimingsContract.TABLE_NAME}/#", TIMINGS_ID)
//
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME, TASK_DURATIONS)
//        matcher.addURI(CONTENT_AUTHORITY, "${TimingsContract.TABLE_NAME}/#", TASK_DURATIONS_ID)

        return matcher
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate: starts")
        return true
    }

    override fun getType(uri: Uri): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "query: called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "query: match is $match")

        val queryBuilder = SQLiteQueryBuilder()

        when (match) {
            TASKS -> queryBuilder.tables = TasksContract.TABLE_NAME

            TASKS_ID -> {
                queryBuilder.tables = TasksContract.TABLE_NAME
                val taskId = TasksContract.getId(uri)
                queryBuilder.appendWhere("${TasksContract.Columns.ID} = ")       // <-- change method
                queryBuilder.appendWhereEscapeString("$taskId")       // <-- change method
            }

//            TIMINGS -> queryBuilder.tables = TimingsContract.TABLE_NAME
//
//            TIMINGS_ID -> {
//                queryBuilder.tables = TimingsContract.TABLE_NAME
//                val timingId = TimingsContract.getId(uri)
//                queryBuilder.appendWhere("${TimingsContract.Columns.ID} = ")   // <-- and here
//                queryBuilder.appendWhereEscapeString("$timingId")   // <-- and here
//            }
//
//            TASK_DURATIONS -> queryBuilder.tables = DurationsContract.TABLE_NAME
//
//            TASK_DURATIONS_ID -> {
//                queryBuilder.tables = DurationsContract.TABLE_NAME
//                val durationId = DurationsContract.getId(uri)
//                queryBuilder.appendWhere("${DurationsContract.Columns.ID} = ")   // <-- and here
//                queryBuilder.appendWhereEscapeString("$durationId")   // <-- and here
//            }
//
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        val db = AppDatabase.getInstance(context).readableDatabase
        val cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        Log.d(TAG, "query: rows in returned cursor = ${cursor.count}") // TODO remove this line

        return cursor
    }

    override fun insert(p0: Uri, p1: ContentValues): Uri? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(p0: Uri, p1: ContentValues, p2: String?, p3: Array<String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}