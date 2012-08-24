package cn.gbase.pagerank.mapreduce

class PageRank { 
	static def mapFunction = {a,b,c-> a.eachWithIndex{ v , idx-> c[idx]= v[0]*b[0]+v[1]*b[1] } }
	static def recuceFunction = {a,b,c ->a.eachWithIndex{ num, idx -> c[idx] = num+b[idx];}}
	static def overFunction = {history,setp,boolc->history[setp-2].eachWithIndex{h,idx->boolc[idx] =Math.abs(h-history[setp-1][idx])<overThrold}}
	static def overSetp = 1000;
	static def overThrold = 0.00000001;
	
	static main(args) {
		//{0d,0d,0d,0d},{0.33d,0d,0d,1d},{0.33d,0.5d,0d,0d},{0.33d,0.5d,1d,0d}
		def nodeA = [[0d,0d],[0.33d,0d],[0.33d,0.5d],[0.33d,0.5d]]
		def nodeB = [[0d,0d],[0d,1d],[0d,0d],[1d,0d]]
		
		def qA = [1,1]
		def qB = [1,1]
		def setp = 0;
		def history = [];
		while(true){
			setp++;
			
			println "-----------no. $setp------------"
			
			def bRduceA =[] ;
			def bRduceB = [];
			
			mapFunction(nodeA,qA,bRduceA);
			mapFunction(nodeB,qB,bRduceB);
			
			println "GA"+bRduceA;
			println "GB"+bRduceB;
			
			def reduce = [];
			recuceFunction(bRduceA,bRduceB,reduce);
			
			history+=[reduce];
			
			println "q "+reduce;
			
			qA = reduce[0,1];
			qB = reduce[2,3];
			
			if(setp>overSetp) {println "sept over!!!";break;}
			def boolc = [false];
			overFunction(history,setp,boolc);
			println boolc;
			if(setp>1 && boolc.every{it==true}) {println "constringency!!!"; break;}
			
		}
		println "";
		history.each {it-> println it;}
		
	}
}
