package com.lnkov.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val recipeId = it.getInt(Constants.ARG_CATEGORY_ID)
            Log.d("!!!", "Id: $recipeId")
        }


    }

}