// package com.nexters.misik.webview
//
// import android.content.Context
// import android.os.Bundle
// import android.view.LayoutInflater
// import android.view.View
// import android.view.ViewGroup
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.layout.safeDrawingPadding
// import androidx.compose.material3.Scaffold
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.platform.ComposeView
// import androidx.fragment.app.Fragment
// import com.nexters.misik.preview.PreviewService
// import com.nexters.misik.preview.di.PreviewEntryPoint
// import dagger.hilt.android.AndroidEntryPoint
// import dagger.hilt.android.EntryPointAccessors
//
// @AndroidEntryPoint
// class WebViewFragment : Fragment() {
//
//    private lateinit var previewService: PreviewService
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        previewService = EntryPointAccessors.fromFragment(this, PreviewEntryPoint::class.java)
//            .previewService()
//        previewService.init(requireActivity())
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        return ComposeView(requireContext()).apply {
//            setContent {
//                Scaffold(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .safeDrawingPadding(),
//                ) { innerPadding ->
//                    WebViewScreen(
//                        previewService,
//                        modifier = Modifier
//                            .padding(innerPadding)
//                            .safeDrawingPadding(),
//                    )
//                }
//            }
//        }
//    }
// }
