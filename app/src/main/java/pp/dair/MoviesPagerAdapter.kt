package pp.dair

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pp.dair.fragments.PrivateNotesFragment
import pp.dair.fragments.PublicNotesFragment


// 1
class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3;
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return PrivateNotesFragment()
            }

            1 -> {
                return PublicNotesFragment()
            }
        }
        return PublicNotesFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Мои заметки"
            }
            1 -> {
                return "Общие заметки"
            }
        }
        return super.getPageTitle(position)
    }

}