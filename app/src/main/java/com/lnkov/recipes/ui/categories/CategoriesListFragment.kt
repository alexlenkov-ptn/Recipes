package com.lnkov.recipes.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lnkov.recipes.R
import com.lnkov.recipes.RecipeApplication
import com.lnkov.recipes.databinding.FragmentListCategoriesBinding
import com.lnkov.recipes.model.Category

class CategoriesListFragment : Fragment() {
    private val binding by lazy { FragmentListCategoriesBinding.inflate(layoutInflater) }

    private lateinit var viewModel: CategoriesViewModel
    private lateinit var categoriesListAdapter: CategoriesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipeApplication).appContainer
        viewModel = appContainer.categoriesListViewModelFactory.create()
    }

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
        categoriesListAdapter = CategoriesListAdapter(emptyList())

        viewModel.categoryUiState.observe(viewLifecycleOwner)
        { categoriesState: CategoriesViewModel.CategoriesUiState ->
            Log.d("CategoriesListFragment", "categories state: ${categoriesState.categories}")

            if (categoriesState.categories == null) {
                Toast.makeText(context, R.string.toast_error_message, Toast.LENGTH_LONG).show()
            } else {
                categoriesListAdapter.updateData(categoriesState.categories)
            }

            binding.rvCategories.adapter = categoriesListAdapter

            categoriesListAdapter.apply {
                setOnItemClickListener(
                    object : CategoriesListAdapter.OnItemClickListener {
                        override fun onItemClick(categoryId: Int) {
                            categoriesState.categories?.find { it.id == categoryId }
                                ?.let { transferCategoryToNext(it) }
                            Log.d("CategoriesListFragment", "create listener")
                        }
                    })
            }
        }
    }

    private fun transferCategoryToNext(category: Category) {
        if (category == null) {
            Log.e("CategoriesListFragment", "Nav error")
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
            throw IllegalStateException("Категория с ID: ${category.id} не найдена")
        }

        Log.d("CategoriesListFragment", "$category")

        val action =
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                category
            )

        findNavController().navigate(action)
    }

}