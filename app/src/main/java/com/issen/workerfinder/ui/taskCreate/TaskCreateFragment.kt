package com.issen.workerfinder.ui.taskCreate

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.issen.workerfinder.R
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInFullUser
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.enums.PriorityTypes
import com.issen.workerfinder.ui.misc.PrioritySpinnerAdapter
import kotlinx.android.synthetic.main.dialog_calendarview_picker.view.*
import kotlinx.android.synthetic.main.dialog_day_interval_picker.view.*
import kotlinx.android.synthetic.main.fragment_task_create.*
import kotlinx.android.synthetic.main.fragment_task_create.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class TaskCreateFragment : Fragment() {

    private val CAMERA_CODE = 0
    private val GALLERY_CODE = 1

    private lateinit var taskCreateViewModel: TaskCreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_task_create, container, false)
        taskCreateViewModel = ViewModelProvider(this).get(TaskCreateViewModel::class.java)

        val tempModel = taskCreateViewModel.generateMockupModel()
        root.new_task_title.setText(tempModel.taskTitle)
        root.new_task_description.setText(tempModel.taskDescription)
        root.new_task_worker.setText(currentLoggedInFullUser!!.userData.firebaseKey)
//        root.new_task_category.setText(tempModel.category)
        root.new_task_date.setText(tempModel.nextCompletionDate)

        val prioritySpinnerAdapter = PrioritySpinnerAdapter(requireContext())
        root.new_task_priority_spinner.adapter = prioritySpinnerAdapter
        root.new_task_priority_spinner.setSelection(prioritySpinnerAdapter.getPosition(PriorityTypes.NORMAL))

        setOnClickListeners(root)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                saveTask()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveTask() {
        val type = getSelectedCyclicType()
        taskCreateViewModel.insert(
            TaskModel(
                0,
                new_task_title.text.toString(),
                new_task_description.text.toString(),
                "zbysiu",
                new_task_worker.text.toString(),
                new_task_date.text.toString(),
                type,
                Date(),
                PriorityTypes.NORMAL.toString(),
                CompletionTypes.ONGOING.toString(),
                ""
            )
        )
        findNavController().popBackStack()
    }

    private fun getSelectedCyclicType() = when (new_task_cyclic_radio.checkedRadioButtonId) {
        R.id.new_task_cyclic_none -> {
            CyclicTypes.NONE.toString()
        }
        R.id.new_task_cyclic_weekday -> {
            CyclicTypes.WEEKDAY.toString()
        }
        R.id.new_task_cyclic_monthday -> {
            CyclicTypes.MONTHDAY.toString()
        }
        R.id.new_task_cyclic_yearday -> {
            CyclicTypes.YEARDAY.toString()
        }
        R.id.new_task_cyclic_day -> {
            CyclicTypes.DAY.toString()
        }
        else -> {
            "Beware of the errors"
        }
    }

    private fun setOnClickListeners(root: View) {
        root.new_task_cyclic_none.setOnClickListener {
            taskCreateViewModel.clearDates()
        }

        root.new_task_cyclic_weekday.setOnClickListener {
            taskCreateViewModel.clearDates()
            showWeekdayPicker()
        }
        root.new_task_cyclic_monthday.setOnClickListener {
            taskCreateViewModel.clearDates()
            showMonthdayPicker()
        }
        root.new_task_cyclic_yearday.setOnClickListener {
            taskCreateViewModel.clearDates()
            showYeardayPicker()
        }
        root.new_task_cyclic_day.setOnClickListener {
            taskCreateViewModel.clearDates()
            showDayIntervalPicker()
        }
        root.new_task_gallery.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, GALLERY_CODE)
        }
        root.new_task_camera.setOnClickListener {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, CAMERA_CODE)
        }
    }

    private fun showWeekdayPicker() {
        val weekdays = resources.getStringArray(R.array.weekdays)
        val checkedWeekdays = BooleanArray(weekdays.size) { false }
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.apply {
            setMultiChoiceItems(
                weekdays,
                checkedWeekdays
            ) { dialogInterface: DialogInterface, position: Int, isChecked: Boolean ->
                checkedWeekdays[position] = isChecked
            }
            setTitle("Wybierz dni tygodnia: ")
            setPositiveButton("Zatwierdź") { dialogInterface, i ->
                checkedWeekdays.forEachIndexed { index, value ->
                    if (value) {
                        taskCreateViewModel.saveDate(index.toString())
                        Toast.makeText(requireContext(), weekdays[index], Toast.LENGTH_SHORT).show()
                    }
                }
            }
            setNeutralButton("Anuluj") { dialogInterface, i ->

            }
            create()
            show()
        }
    }

    private fun showMonthdayPicker() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_calendarview_picker, null)
        val calendarView = dialogView.calendar
        calendarView.setHeaderVisibility(View.GONE)

        calendarView.setMinimumDate(Calendar.getInstance().apply {
            set(Calendar.DATE, 1)
            add(Calendar.DATE, -1)
        })
        calendarView.setMaximumDate(Calendar.getInstance().apply {
            add(Calendar.MONTH, 1)
            set(Calendar.DATE, 1)
            add(Calendar.DATE, -1)
        })

        builder.apply {
            setView(dialogView)
            setPositiveButton("Zatwierdź") { dialogInterface, i ->
                val selectedDates = dialogView.calendar.selectedDates
                val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

                selectedDates.forEach {
                    taskCreateViewModel.saveDate(format.format(it.time))
                    Toast.makeText(requireContext(), format.format(it.time), Toast.LENGTH_SHORT).show()
                }
            }
            setNeutralButton("Anuluj") { dialogInterface, i ->

            }
            create()
            show()
        }
    }

    private fun showYeardayPicker() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_calendarview_picker, null)
        val calendarView = dialogView.calendar

        calendarView.setMinimumDate(Calendar.getInstance().apply {
            set(Calendar.MONTH, 0)
            set(Calendar.DATE, 0)
        })
        calendarView.setMaximumDate(Calendar.getInstance().apply {
            add(Calendar.YEAR, 1)
            set(Calendar.MONTH, 0)
            set(Calendar.DATE, 0)
            add(Calendar.DATE, -1)
        })

        builder.apply {
            setView(dialogView)
            setPositiveButton("Zatwierdź") { dialogInterface, i ->
                val selectedDates = dialogView.calendar.selectedDates
                val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                selectedDates.forEach {
                    taskCreateViewModel.saveDate(format.format(it.time))
                    Toast.makeText(requireContext(), format.format(it.time), Toast.LENGTH_SHORT).show()
                }
            }
            setNeutralButton("Anuluj") { dialogInterface, i ->

            }
            create()
            show()
        }
    }

    private fun showDayIntervalPicker() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_day_interval_picker, null)

        builder.apply {
            setView(dialogView)
            setTitle("Wybierz częstotliwość powtarzania: ")
            setPositiveButton("Zatwierdź") { dialogInterface, i ->
                taskCreateViewModel.saveDate(dialogView.day_interval.text.toString())
                Toast.makeText(requireContext(), dialogView.day_interval.text.toString(), Toast.LENGTH_SHORT).show()
            }
            setNeutralButton("Anuluj") { dialogInterface, i ->

            }
            create()
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_CODE -> if (resultCode == RESULT_OK) {
                val extras = data?.extras
                val image = extras!!["data"] as Bitmap?
                if (image != null) {
                    val relativeLocation = Environment.DIRECTORY_PICTURES + File.separator + "WorkerFinder"

                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.ImageColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //this one
                            put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
                        }
                    }

                    val resolver = requireActivity().contentResolver
                    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    try {

                        uri?.let { uri ->
                            val stream = resolver.openOutputStream(uri)

                            stream?.let { stream ->
                                if (!image.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                                    throw IOException("Failed to save bitmap.")
                                }
                            } ?: throw IOException("Failed to get output stream.")

                        } ?: throw IOException("Failed to create new MediaStore record")

                    } catch (e: IOException) {
                        if (uri != null) {
                            resolver.delete(uri, null, null)
                        }
                        throw IOException(e)
                    } finally {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                        taskCreateViewModel.savePhoto(uri.toString())
                        addImageToGrid(uri)
                    }

                }
            }
            GALLERY_CODE -> if (resultCode == RESULT_OK) {
                val image: Uri? = data?.data
                if (image != null) {
                    taskCreateViewModel.savePhoto(image.toString())
                    addImageToGrid(image)
                }
            }
        }
    }

    private fun addImageToGrid(uri: Uri?) {
        val imageView = ImageView(requireContext()).apply {
            id = (taskCreateViewModel.photos.size - 1)
            setImageURI(uri)
        }
        task_photos_grid.addView(imageView)
    }

}