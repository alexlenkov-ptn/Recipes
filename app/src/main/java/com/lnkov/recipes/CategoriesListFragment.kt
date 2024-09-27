package com.lnkov.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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


        categoriesListAdapter.setOnItemClickListener(
            object : CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    openRecipesByCategoryId(categoryId)
                }
            })

        binding.rvCategories.adapter = categoriesListAdapter

    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        var bundle: Bundle? = null

        val category = STUB.getCategories().find {
            it.id == categoryId
        }
        category?.let {
            bundle = bundleOf(
                Constants.ARG_CATEGORY_ID to category.id,
                Constants.ARG_CATEGORY_NAME to category.title,
                Constants.ARG_CATEGORY_IMAGE_URL to category.imageUrl
            )
        }
        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

}