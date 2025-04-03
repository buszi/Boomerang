package io.buszi.boomerang

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.buszi.boomerang.core.BoomerangStore
import io.buszi.boomerang.core.BoomerangStoreHost
import io.buszi.boomerang.databinding.FragmentALayoutBinding
import io.buszi.boomerang.databinding.FragmentBLayoutBinding
import io.buszi.boomerang.databinding.FragmentCLayoutBinding
import io.buszi.boomerang.fragment.catchBoomerangWithLifecycleEvent
import io.buszi.boomerang.fragment.createOrRestoreDefaultBoomerangStore
import io.buszi.boomerang.fragment.findBoomerangStore
import io.buszi.boomerang.fragment.saveDefaultBoomerangStoreState

class FullFragmentPreviewActivity : AppCompatActivity(), BoomerangStoreHost {

    override var boomerangStore: BoomerangStore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createOrRestoreDefaultBoomerangStore(savedInstanceState)
        setContentView(R.layout.full_fragment_preview_activity_layout)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveDefaultBoomerangStoreState(outState)
    }
}

class FragmentA : Fragment(R.layout.fragment_a_layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catchBoomerangWithLifecycleEvent("fragment_a") { bundle ->
            bundle.getString("result")?.let { result ->
                view?.let { FragmentALayoutBinding.bind(it) }
                    ?.resultLabel
                    ?.text = result
                savedState = result
            }
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(FragmentALayoutBinding.bind(view)) {
            navigateButton.setOnClickListener {
                findNavController().navigate(R.id.navigateB)
            }
            clearButton.setOnClickListener {
                savedState = ""
                resultLabel.text = ""
            }
        }
    }

    private companion object {
        // IK this is super wrong, but it is just for easier testing
        private var savedState = ""
    }
}

class FragmentB : Fragment(R.layout.fragment_b_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(FragmentBLayoutBinding.bind(view)) {
            navigateButton.setOnClickListener {
                findNavController().navigate(R.id.navigateC)
            }
            dropValueButton.setOnClickListener {
                findBoomerangStore().dropValue("fragment_a")
            }
            recreateButton.setOnClickListener {
                activity?.recreate()
            }
        }
    }
}

class FragmentC : Fragment(R.layout.fragment_c_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(FragmentCLayoutBinding.bind(view)) {
            navigateBackButton.setOnClickListener {
                findBoomerangStore().storeValue("fragment_a", bundleOf("result" to "hello"))
                findNavController().popBackStack()
            }
        }
    }
}
