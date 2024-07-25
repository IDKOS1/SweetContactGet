package com.example.sweetcontactget.fragments.Contact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetcontactget.adapter.ContactAdapter
import com.example.sweetcontactget.data.Contact
import com.example.sweetcontactget.data.DataObject.contactData
import com.example.sweetcontactget.R
import com.example.sweetcontactget.databinding.FragmentAllContactBinding
import com.example.sweetcontactget.util.CustomDividerDecoration
import com.example.sweetcontactget.util.KoreanMatcher
import com.example.sweetcontactget.util.TopSnappedSmoothScroller
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerView

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AllContactFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAllContactBinding? = null
    private val binding get() = _binding!!
    private val contactAdapter by lazy { ContactAdapter().apply { submitList(contactData.toList()) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllContactBinding.inflate(inflater, container, false)
        val recyclerView = binding.rvAllContactFragment

        recyclerView.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(this.context)
            val dividerColor = ContextCompat.getColor(context,R.color.secondary)
            val itemDecoration = CustomDividerDecoration(context, height = 3f, dividerColor,0f ,100f)
            addItemDecoration(itemDecoration)
        }


        // 패스트 스크롤 정의
        binding.fastscroller.setupWithRecyclerView(recyclerView,
            {position ->
                val item = contactData[position]
                when(item){
                    is Contact.ContactIndex -> FastScrollItemIndicator.Text(item.letter)
                    is Contact.SweetieInfo -> FastScrollItemIndicator.Text(KoreanMatcher.getConsonant(item.name.first()).toString())
                    else -> null
                }
            }
        )

        binding.fastscrollerThumb.setupWithFastScroller(binding.fastscroller)

        //패스트 스크롤 동작 커스텀
        binding.fastscroller.apply {
            useDefaultScroller = false
            itemIndicatorSelectedCallbacks += object : FastScrollerView.ItemIndicatorSelectedCallback{
                override fun onItemIndicatorSelected(
                    indicator: FastScrollItemIndicator,
                    indicatorCenterY: Int,
                    itemPosition: Int
                ) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val smoothScroller = TopSnappedSmoothScroller(recyclerView.context)
                    smoothScroller.targetPosition = itemPosition
                    layoutManager.startSmoothScroll(smoothScroller)

                }
            }
        }




        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun search(searchTarget: CharSequence?) {
        contactAdapter.filter.filter(searchTarget)
    }
}