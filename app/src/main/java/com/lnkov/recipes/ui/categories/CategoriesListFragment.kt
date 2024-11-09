package com.lnkov.recipes.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lnkov.recipes.R
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.STUB
import com.lnkov.recipes.databinding.FragmentListCategoriesBinding
import com.lnkov.recipes.model.Category

class CategoriesListFragment : Fragment() {
    private val binding by lazy { FragmentListCategoriesBinding.inflate(layoutInflater) }

    private lateinit var categoriesListAdapter: CategoriesListAdapter
    private val viewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadCategories()
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        categoriesListAdapter = CategoriesListAdapter(emptyList()).apply {
            setOnItemClickListener(
                object : CategoriesListAdapter.OnItemClickListener {
                    override fun onItemClick(categoryId: Int) {
                        openRecipesByCategoryId(categoryId)
                    }
                })
        }

        viewModel.categoryUiState.observe(viewLifecycleOwner)
        { categoriesState: CategoriesViewModel.CategoriesUiState ->
            Log.d("!!!", "categories state: ${categoriesState.categories}")

            categoriesListAdapter.updateData(categoriesState.categories)

            binding.rvCategories.adapter = categoriesListAdapter
        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB.getCategoryById(categoryId)


        if (category == null) {
            Log.e("CategoriesListFragment", "Nav error")
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
            throw IllegalStateException("Категория с ID: $categoryId не найдена")
        }

        Log.d("CategoriesListFragment", "$category")


        val action =
            CategoriesListFragmentDirections.
            actionCategoriesListFragmentToRecipesListFragment(category)

        findNavController().navigate(action)
    }

}