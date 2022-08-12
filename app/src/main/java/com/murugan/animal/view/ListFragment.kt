package com.murugan.animal.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


import com.murugan.animal.R
import com.murugan.animal.model.Animal
import com.murugan.animal.viewmodel.ListViewModel


class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val listAdapter = AnimalListAdapter(arrayListOf())

    private fun animalLiveDataObserver(view: View) = Observer<List<Animal>> { list ->
        list?.let {
            view.findViewById<RecyclerView>(R.id.animalList).visibility = View.VISIBLE
            listAdapter.updateAnimalList(it)
        }
    }

    private fun loadingLiveDataObserver(view: View) = Observer<Boolean> { loading ->
        view.findViewById<ProgressBar>(R.id.loadingView).visibility =
            if (loading) View.VISIBLE else View.GONE
    }

    private fun errorLiveDataObserver(view: View) = Observer<Boolean> { isError ->
        view.findViewById<TextView>(R.id.listError).visibility =
            if (isError) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        viewModel.animal.observe(viewLifecycleOwner, animalLiveDataObserver(view))
        viewModel.loading.observe(viewLifecycleOwner, loadingLiveDataObserver(view))
        viewModel.loadError.observe(viewLifecycleOwner, errorLiveDataObserver(view))
        viewModel.refresh()

        view.findViewById<RecyclerView>(R.id.animalList).apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter
        }

        view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout).setOnRefreshListener {
            view.findViewById<TextView>(R.id.listError).visibility = View.GONE
            view.findViewById<RecyclerView>(R.id.animalList).visibility = View.GONE
            view.findViewById<ProgressBar>(R.id.loadingView).visibility = View.VISIBLE
            viewModel.hardRefresh()
            view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout).isRefreshing = false

        }


    }

}