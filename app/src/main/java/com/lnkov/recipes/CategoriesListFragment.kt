package com.lnkov.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.lnkov.recipes.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private val binding by lazy { FragmentListCategoriesBinding.inflate(layoutInflater) }

    private lateinit var categoriesListAdapter: CategoriesListAdapter

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

        openRecipesByCategoryId()

        binding.rvCategories.adapter = categoriesListAdapter

    }

    private fun openRecipesByCategoryId() {
        categoriesListAdapter.setOnItemClickListener(
            object : CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick() {
                    parentFragmentManager.commit {
                        replace<RecipesListFragment>(R.id.mainContainer)
                        setReorderingAllowed(true)
                        addToBackStack(null)
                    }
                }
            })
    }

}