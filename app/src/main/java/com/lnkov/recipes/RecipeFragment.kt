package com.lnkov.recipes

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lnkov.recipes.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }

    private lateinit var ingredientsListAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter

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
            var recipe: Recipe? = null

            recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(Constants.ARG_RECIPE, Recipe::class.java)
            } else {
                it.getParcelable(Constants.ARG_RECIPE)
            }

            Log.d("!!!", "recipeDeprecatedMethod title: ${recipe?.title}")

            if (recipe != null) {
                initUI(recipe)
                initRecycler(recipe)
            }
        }

    }

    private fun initUI(recipe: Recipe) {
        var heartIconStatus = false

        var favoritesSet : MutableSet<String?>? = mutableSetOf(null)
        if (getFavorites() != null) favoritesSet = getFavorites()

        val recipeIdString = recipe.id.toString()


        val drawable: Drawable? = try {
            Drawable.createFromStream(
                context?.assets?.open(recipe.imageUrl ?: ""),
                null
            )
        } catch (e: Exception) {
            Log.d("!!!", "Image not found: ${recipe.imageUrl}")
            null
        }

        binding.apply {
            ivBcgRecipe.setImageDrawable(drawable)
            ivBcgRecipe.contentDescription = "Image: ${recipe.imageUrl}"
            tvRecipe.text = recipe.title

            ibIcHeart.apply {
                setImageResource(R.drawable.ic_heart_empty_recipe)

                if (favoritesSet?.contains(recipeIdString) == true) {
                    heartIconStatus = true
                    setImageResource(R.drawable.ic_heart_recipe)
                }
                else {
                    heartIconStatus = false
                }

// todo я вообще чего-то забыл по какому принципу у нас включается иконка
                // todo нужно пересмотреть RA-16 и эту реализацию

                setOnClickListener {
                    heartIconStatus = !heartIconStatus

                    when(heartIconStatus) {
                        true -> {
                            setImageResource(R.drawable.ic_heart_recipe)
                            favoritesSet?.add(recipeIdString)
                            saveFavorites(favoritesSet?.toSet() as Set<String>)
                        }

                        false -> {
                            setImageResource(R.drawable.ic_heart_empty_recipe)
                            favoritesSet?.remove(recipeIdString)
                            saveFavorites(favoritesSet?.toSet() as Set<String>)
                        }
                    }
                }
            }
        }
    }

    private fun initRecycler(recipe: Recipe) {
        ingredientsListAdapter = IngredientsAdapter(recipe.ingredients)
        methodAdapter = MethodAdapter(recipe.method)

        val decorator = MaterialDividerItemDecoration(
            requireContext(),
            MaterialDividerItemDecoration.VERTICAL,
        ).apply {
            isLastItemDecorated = false
            dividerInsetStart = resources.getDimensionPixelSize(R.dimen.main_space_12)
            dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.main_space_12)
            dividerColor = resources.getColor(R.color.gray)
        }

        binding.apply {
            rvRecipeIngredients.addItemDecoration(decorator)
            rvRecipeCookingMethod.addItemDecoration(decorator)
            rvRecipeIngredients.adapter = ingredientsListAdapter
            rvRecipeCookingMethod.adapter = methodAdapter

            sbCountsOfRecipes.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    @SuppressLint("SetTextI18n")
                    override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                        ingredientsListAdapter.updateIngredients(progress)
                        tvNumberOfPortions.text = progress.toString()
                        rvRecipeIngredients.adapter = ingredientsListAdapter
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                }
            )
        }


    }

    fun saveFavorites(stringSet: Set<String>) {

        val sharePrefs = context?.getSharedPreferences(
            getString(R.string.com_lnkov_recipes_FAVORITES_FILE_KEY), Context.MODE_PRIVATE) ?: return

        with(sharePrefs.edit()) {
            putStringSet("favorites_key", stringSet)
            apply()
        }
    }

    fun getFavorites() : MutableSet<String?>? {
        val sharePrefs = context?.getSharedPreferences(
            getString(R.string.com_lnkov_recipes_FAVORITES_FILE_KEY), Context.MODE_PRIVATE) ?: return null

        val stringHashSet : Set<String>  = HashSet<String>()

        return sharePrefs.getStringSet("favorites_key", stringHashSet)

    }


}