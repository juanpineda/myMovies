package com.juanpineda.mymovies.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.juanpineda.mymovies.R
import com.juanpineda.mymovies.databinding.DialogVoteBinding

class VoteDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "VoteDialogFragment"
        const val ARGUMENT_VOTE = "ARGUMENT_VOTE"
    }

    class Builder {
        private var setOnVoteClickListener: ((Float) -> Unit)? = null
        private var setOnRankingBarChangeListener: ((Float) -> Unit)? = null
        private val arguments = Bundle()

        fun setOnVoteClickListener(listener: ((Float) -> Unit)? = null) = apply {
            setOnVoteClickListener = listener
        }

        fun setOnRankingBarChangeListener(listener: ((Float) -> Unit)? = null) = apply {
            setOnRankingBarChangeListener = listener
        }

        fun setVote(vote: Float) = apply {
            arguments.putFloat(ARGUMENT_VOTE, vote)
        }

        fun create(): VoteDialogFragment {
            val dialog = VoteDialogFragment()
            dialog.arguments = arguments
            dialog.setOnVoteClickListener = setOnVoteClickListener
            dialog.setOnRankingBarChangeListener = setOnRankingBarChangeListener
            return dialog
        }
    }

    private var setOnVoteClickListener: ((Float) -> Unit)? = null
    private var setOnRankingBarChangeListener: ((Float) -> Unit)? = null
    lateinit var binding: DialogVoteBinding

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        view?.post {
            val parent = view?.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior
            bottomSheetBehavior.peekHeight = view?.measuredHeight ?: 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogVoteBinding.inflate(layoutInflater)
        binding.buttonVoteDialog.setOnClickListener {
            setOnVoteClickListener?.invoke(binding.ratingBar.rating)
            dismiss()
        }
        binding.textViewCloseDialog.setOnClickListener {
            dismiss()
        }
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, value, b ->
            setOnRankingBarChangeListener?.invoke(value)
        }
        arguments?.apply {
            binding.ratingBar.rating = getFloat(ARGUMENT_VOTE, 0f)
        }
        return binding.root
    }
}