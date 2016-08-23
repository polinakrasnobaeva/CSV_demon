package normalModule;
public class Pair<type1, type2>{
		private type1 el1;
		private type2 el2;
		
		public Pair(type1 el1, type2 el2){
			this.el1 = el1;
			this.el2 = el2;
		}

		public type1 getEl1(){
			return this.el1;
		}
		
		public type2 getEl2(){
			return this.el2;
		}
	}
