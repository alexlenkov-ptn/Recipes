package com.lnkov.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.lnkov.recipes.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private val binding by lazy { FragmentListCategoriesBinding.inflate(layoutInflater) }

    private lateinit var categoriesListAdapter : CategoriesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        categoriesListAdapter = CategoriesListAdapter(STUB.getCategories())
        binding.rvCategories.adapter = categoriesListAdapter

        categoriesListAdapter.setOnItemClickListener(

            object : CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick() {
                    Log.d("FragmentTransaction", "Attempting to replace fragment")
                    parentFragmentManager.commit {

                        replace(R.id.linearLayoutFragmentListCategories, RecipesListFragment())
                        setReorderingAllowed(true)
                        addToBackStack(null)
                    }

                }
            })
    }

}