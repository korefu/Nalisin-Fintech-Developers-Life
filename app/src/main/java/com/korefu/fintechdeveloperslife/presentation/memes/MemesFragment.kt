package com.korefu.fintechdeveloperslife.presentation.memes

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.korefu.fintechdeveloperslife.MyApp
import com.korefu.fintechdeveloperslife.R
import com.korefu.fintechdeveloperslife.data.model.MemeModel
import com.korefu.fintechdeveloperslife.data.model.SortingType
import com.korefu.fintechdeveloperslife.databinding.FragmentMemesBinding
import com.korefu.fintechdeveloperslife.utils.PagingStatus

class MemesFragment : Fragment() {

    companion object {
        const val SORTING_TYPE_KEY = "sorting"
        fun newInstance(sortingType: SortingType): MemesFragment {
            val fragment = MemesFragment()
            val bundle = Bundle()
            bundle.putString(SORTING_TYPE_KEY, sortingType.name)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel: MemesViewModel by viewModels {
        MemesViewModelImpl.Factory(
            (activity?.application as MyApp).appComponent.memesViewModelAssistedFactory(),
            savedSortingType
        )
    }
    private var _binding: FragmentMemesBinding? = null
    private val binding get() = _binding!!
    private val savedSortingType: SortingType
        get() = SortingType.valueOf(arguments?.getString(SORTING_TYPE_KEY) ?: "TOP")
    private val imageLoader by lazy {
        ImageLoader.Builder(requireContext())
            .componentRegistry {
                if (SDK_INT >= Build.VERSION_CODES.P)
                    add(ImageDecoderDecoder(requireContext()))
                else
                    add(GifDecoder())
            }.build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemesBinding.inflate(inflater, container, false)
        viewModel.loadMemes()
        viewModel.items.observe(viewLifecycleOwner) {
            val position = viewModel.position.value
            if (position != null)
                showItem(it[position])
        }
        binding.buttonNext.setOnClickListener {
            viewModel.nextItem()
        }
        binding.buttonBack.setOnClickListener {
            viewModel.previousItem()
        }
        binding.loadingOrError.retryButton.setOnClickListener { viewModel.loadMemes() }
        viewModel.position.observe(viewLifecycleOwner) {
            binding.buttonBack.isEnabled = it != 0
            val items = viewModel.items.value
            if (items != null) {
                binding.buttonNext.isEnabled = it < items.size
                showItem(items[it])
            }
        }
        return binding.root
    }

    private fun showItem(item: Any) {
        hideLoadingOrError()
        when (item) {
            is MemeModel -> {
                binding.buttonNext.isEnabled = true
                binding.memeCard.visibility = View.VISIBLE
                binding.memeImageView.load(item.gifURL, imageLoader) {
                    listener(onStart = {
                        showLoading()
                    }, onError = { _, _ ->
                        showError(R.string.network_error_msg)
                    }, onSuccess = { _, _ ->
                        hideLoadingOrError()
                    })
                }
                binding.textViewDescription.text = item.description
            }
            is PagingStatus -> {
                binding.buttonNext.isEnabled = false
                binding.memeCard.visibility = View.GONE
                when (item) {
                    PagingStatus.LOADING -> showLoading()
                    PagingStatus.ERROR -> showError(R.string.network_error_msg)
                    PagingStatus.END -> showError(R.string.paging_end_msg)
                }
            }
        }
    }

    private fun showLoading() {
        binding.loadingOrError.progressBar.visibility = View.VISIBLE
        binding.loadingOrError.errorLayout.visibility = View.GONE
    }

    private fun showError(textResId: Int) {
        binding.loadingOrError.progressBar.visibility = View.GONE
        binding.loadingOrError.errorLayout.visibility = View.VISIBLE
        binding.loadingOrError.errorText.text = getString(textResId)
    }

    private fun hideLoadingOrError() {
        binding.loadingOrError.progressBar.visibility = View.GONE
        binding.loadingOrError.errorLayout.visibility = View.GONE
    }
}
