package com.mindera.rocketscience.home.rocketlaunch.itself

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.mindera.rocketscience.databinding.DialogRocketLaunchBinding

class RocketLaunchBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "RocketLaunchItemBottomSheet"
        const val ARG_MISSION_NAME = "ARG_MISSION_NAME"
        const val ARG_ARTICLE_URL = "ARG_ARTICLE_URL"
        const val ARG_WIKIPEDIA_URL = "ARG_WIKIPEDIA_URL"
        const val ARG_VIDEO_URL = "ARG_VIDEO_URL"

        fun newInstance(
            missionName: String,
            articleURL: String?,
            wikipediaUrl: String?,
            videoUrl: String?
        ) = RocketLaunchBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_MISSION_NAME, missionName)
                putString(ARG_ARTICLE_URL, articleURL)
                putString(ARG_WIKIPEDIA_URL, wikipediaUrl)
                putString(ARG_VIDEO_URL, videoUrl)
            }
        }
    }

    private var _binding: DialogRocketLaunchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRocketLaunchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUp()
    }

    private fun setUp() {
        val missionName = arguments?.getString(ARG_MISSION_NAME)!!
        val articleURL = arguments?.getString(ARG_ARTICLE_URL)
        val wikipediaUrl = arguments?.getString(ARG_WIKIPEDIA_URL)
        val videoUrl = arguments?.getString(ARG_VIDEO_URL)

        binding.run {
            txtMissionName.text = missionName

            handleButton(btnArticle, articleURL)
            handleButton(btnWikipedia, wikipediaUrl)
            handleButton(btnVideo, videoUrl)
        }
    }

    private fun handleButton(button: MaterialButton, forUrl: String?) {
        if (forUrl.isNullOrEmpty()) {
            button.isEnabled = false
        } else {
            button.setOnClickListener {
                openBrowser(forUrl)
            }
        }
    }

    private fun openBrowser(withUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(withUrl)
        startActivity(intent)
    }
}