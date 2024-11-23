package com.lnkov.recipes.ui.recipes.recipe_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lnkov.recipes.R
import com.lnkov.recipes.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private val binding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }

    private lateinit var recipesListAdapter: RecipesListAdapter

    private val args: RecipesListFragmentArgs by navArgs()

    private val viewModel: RecipesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadCategory(args.category.id)
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {

        recipesListAdapter = RecipesListAdapter(emptyList()).apply {
            setOnItemClickListener(
                object : RecipesListAdapter.OnItemClickListener {
                    override fun onItemClick(recipeId: Int) {
                        openRecipeByRecipesId(recipeId)
                    }
                }
            )
        }

        viewModel.recipeListUiState.observe(viewLifecycleOwner)
        { recipesListState: RecipesListViewModel.RecipeListUiState ->
            Log.d(
                "RecipesListFragment",
                "category: ${recipesListState.category}"
            )


            if (recipesListState.recipeList == null) {
                Toast.makeText(context, R.string.toast_error_message, Toast.LENGTH_LONG).show()
            } else {
                recipesListAdapter.updateData(recipesListState.recipeList)
                Log.d(
                    "RecipesListFragment",
                    "recipe list: ${recipesListState.recipeList}"
                )

            }

            binding.apply {
                ivBcgRecipeList.contentDescription =
                    "Image: ${recipesListState.category?.description}"

                tvBcgRecipeList.text = recipesListState.category?.title

                Glide.with(requireContext())
                    .load(recipesListState.drawableUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(ivBcgRecipeList)

                rvRecipes.adapter = recipesListAdapter
            }
        }
    }

    private fun openRecipeByRecipesId(recipeId: Int) {

        val action =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)

        findNavController().navigate(action)
    }
}