import {conf} from '@/pina/stores/conf'
import  {direct} from  '@/pina/stores/direct'
import  {menu} from  '@/pina/stores/menu'
import  {tag} from  '@/pina/stores/tag'
import  {user} from  '@/pina/stores/user'

const store = () => {
	return {
		conf:conf(),
		direct:direct(),
		menu:menu(),
		tag:tag(),
		user:user(),

	}
}

export default store
