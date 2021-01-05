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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.databinding.FragmentTaskCreateBinding
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.enums.PriorityTypes
import com.issen.workerfinder.utils.ViewAnimation
import com.issen.workerfinder.utils.toggleArrow
import kotlinx.android.synthetic.main.dialog_calendarview_picker.view.*
import kotlinx.android.synthetic.main.dialog_day_interval_picker.view.*
import kotlinx.android.synthetic.main.fragment_task_create.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class TaskCreateFragment : Fragment(), TaskCreateListener {

    private val CAMERA_CODE = 0
    private val GALLERY_CODE = 1

    private val taskCreateViewModel: TaskCreateViewModel by viewModels {
        TaskCreateViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).taskRepository,
            (requireActivity().application as WorkerFinderApplication).taskPhotoRepository,
            (requireActivity().application as WorkerFinderApplication).taskRepeatDayRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentTaskCreateBinding.inflate(inflater, container, false)
        val tempModel = taskCreateViewModel.generateMockupModel()
        binding.task = TaskModelFull(tempModel, mutableListOf(), mutableListOf(), mutableListOf(), currentLoggedInUserFull!!.userData, currentLoggedInUserFull!!.userData)
        binding.clickListener = this

        val prioritySpinnerAdapter = PrioritySpinnerAdapter(requireContext())
        binding.newTaskPrioritySpinner.adapter = prioritySpinnerAdapter
        binding.newTaskPrioritySpinner.setSelection(prioritySpinnerAdapter.getPosition(PriorityTypes.NORMAL))

        binding.executePendingBindings()
        return binding.root
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
                currentLoggedInUserFull!!.userData.userId,
                "a1",
                new_task_date_container.text.toString(),
                type,
                Date(),
                PriorityTypes.NORMAL.toString(),
                CompletionTypes.ACTIVE.toString(),
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

    private fun showMonthDayPicker() {
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

    private fun showYearDayPicker() {
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
        new_task_photos_grid.addView(imageView)
    }

    private fun toggleSectionInput(view: View, containerView: View) {
        val show = toggleArrow(view)
        if (show) {
            ViewAnimation.expand(containerView)
        } else {
            ViewAnimation.collapse(containerView)
        }
    }

    override fun onHeaderClicked(view: View) {
        toggleSectionInput(
            view.findViewWithTag("viewButton"),
            (view.parent as LinearLayout).findViewWithTag(view.tag.toString() + "Container")
        )
    }

    override fun onAddWorkerClicked() {
        TODO("Not yet implemented")
    }

    override fun onCategoryFastSelectClicked() {
        TODO("Not yet implemented")
    }

    override fun onSelectPictureClicked() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, GALLERY_CODE)
    }

    override fun onTakePictureClicked() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePicture, CAMERA_CODE)
    }

    override fun onCyclicDayClicked() {
        taskCreateViewModel.clearDates()
        showDayIntervalPicker()
    }

    override fun onCyclicYearDayClicked() {
        taskCreateViewModel.clearDates()
        showYearDayPicker()
    }

    override fun onCyclicMonthDayClicked() {
        taskCreateViewModel.clearDates()
        showMonthDayPicker()
    }

    override fun onCyclicWeekdayClicked() {
        taskCreateViewModel.clearDates()
        showWeekdayPicker()
    }

    override fun onCyclicNoneClicked() {
        taskCreateViewModel.clearDates()
    }

    override fun onSetNextDateClicked() {
        TODO("Not yet implemented")
    }

    override fun setRemote(view: View) {
        TODO("Not yet implemented")
    }

}