package com.lnkov.recipes

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.lnkov.recipes.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            var recipe: Recipe? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                recipe = it.getParcelable(Constants.ARG_RECIPE, Recipe::class.java)
            } else {
                recipe = it.getParcelable(Constants.ARG_RECIPE)
            }

            Log.d("!!!", "recipeDeprecatedMethod title: ${recipe?.title}")

            binding.tvRecipe.text = recipe?.title
        }

    }
}