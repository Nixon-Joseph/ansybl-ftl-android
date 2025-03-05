package com.devpirates.ftl

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.devpirates.ftl.databinding.FragmentHomeBinding
import com.devpirates.ftl.recycler.AnsyblConnectionRecyclerView
import com.devpirates.ftl.room.AppDatabase
import com.devpirates.ftl.room.ansyblconnection.AnsyblConnection
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val connections = arrayListOf<AnsyblConnection>()
    private var db: AppDatabase? = null
    private val adapter = AnsyblConnectionRecyclerView(connections)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            super.requireContext(),
            AppDatabase::class.java, "ansybl-connections"
        )
            //.addMigrations()
            .fallbackToDestructiveMigration()
            .build()

        kotlin.concurrent.thread(true, false, null, "GetAnsyblConnections", 1, {
            val allConnections = db!!.ansyblConnectionDao().getAll()
            connections.addAll(allConnections)
            adapter.notifyItemInserted(connections.size - 1)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.ansybl_recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(super.getContext())
        recyclerView?.adapter = adapter

        binding.fab.setOnClickListener { view ->
            val builder = AlertDialog.Builder(super.requireContext());
            val input = EditText(super.requireContext())
            input.hint = "https://domain.com/feed.ansybl"
            builder.setView(input)

            builder.setTitle("Add Ansybl Connection")

            builder.setPositiveButton("Add") { dialog, _ ->
                if (input.text.toString().isEmpty() || !URLUtil.isValidUrl(input.text.toString())) {
                    Toast.makeText(super.requireContext(), "Please enter a valid URL", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                try {
                    asyncGetHttpRequest(
                        endpoint = input.text.toString(),
                        onSuccess = { apiRes ->
                            try {
                                val ansyblConnection = AnsyblConnection(
                                    apiRes.response.id,
                                    apiRes.response.summary,
                                    input.text.toString(),
                                    Instant.now().toString(),
                                    Gson().toJson(apiRes.response)
                                )

                                kotlin.concurrent.thread(true, false, null, "GetAnsyblConnections", 1, {
                                    try {
                                        db!!.ansyblConnectionDao().insertAll(ansyblConnection)
                                    } catch (e: Exception) {
                                        Log.d("Error", e.toString())
                                        // put toast in main thread
                                        super.requireActivity().runOnUiThread {
                                            Toast.makeText(super.requireContext(), "Unable to save connection", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                })
                                connections.add(ansyblConnection)
                                adapter.notifyItemInserted(connections.size - 1)
                            } catch (e: Exception) {
                                Toast.makeText(super.requireContext(), "Error getting ansybl feed for ${input.text}", Toast.LENGTH_LONG).show()
                                Log.d("Error", e.toString())
                            } finally {
                                dialog.dismiss()
                            }
                        },
                        onError = { error ->
                            Toast.makeText(super.requireContext(), "Error getting ansybl feed for ${input.text}", Toast.LENGTH_LONG).show()
                            Log.d("Error", error.toString())
                            dialog.dismiss()
                        }
                    )
                } catch (e: Exception) {
                    Toast.makeText(super.requireContext(), "Error getting ansybl feed for ${input.text}", Toast.LENGTH_LONG).show()
                    Log.d("Error", e.toString())
                }
            }

            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            builder.show()
        }

        adapter.onItemClick = { item ->
            Log.d("Item", item.json)
            val ansyblConnection = parseJson<com.devpirates.ftl.models.AnsyblConnection>(item.json)
            val action = HomeFragmentDirections.actionHomeFragmentToAnsyblItemsFragment(ansyblConnection)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun asyncGetHttpRequest(
        endpoint: String,
        onSuccess: (ApiResponse<com.devpirates.ftl.models.AnsyblConnection>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var responseCode = -1
            try {
                val url = URL(endpoint)
                val openedConnection = url.openConnection() as HttpURLConnection
                openedConnection.requestMethod = "GET"

                responseCode = openedConnection.responseCode
                val reader = BufferedReader(InputStreamReader(openedConnection.inputStream))
                val response = reader.readText()
                val apiResponse = ApiResponse(
                    responseCode,
                    parseJson<com.devpirates.ftl.models.AnsyblConnection>(response)
                )
                print(response)
                reader.close()
                // Call the success callback on the main thread
                launch(Dispatchers.Main) {
                    onSuccess(apiResponse)
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
                // Handle error cases and call the error callback on the main thread
                launch(Dispatchers.Main) {
                    onError(Exception("HTTP Request failed with response code $responseCode"))
                }
            } finally {

            }
        }
    }

    data class ApiResponse<T>(
        val responseCode: Int,
        val response: T
    )

    private inline fun <reified T>parseJson(text: String): T =
        Gson().fromJson(text, T::class.java)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}