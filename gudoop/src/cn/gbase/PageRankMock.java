package cn.gbase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import bsh.EvalError;
import bsh.Interpreter;
/**
 * 
 * 模拟pagerank算法
 * @author jacky
 * @date 2012-8-21
 * @version 0.1
 * 
 * */
public class PageRankMock<T> {
	
	/**
	 * 矩阵
	 * */
	public static class Matrix<T>{
		private T [][] f = null;
		private String name;
		public Matrix(){};
		public Matrix(String name){
			this.name = name;
		}
		public Matrix(T t[][]){
			this.f = t;
			this.name = "";
		}
		public Matrix(String name,T t[][]){
			this.f = t;
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getRowCount(){
			return f.length;
		}
		public int getColumCount(){
			return f[0].length;
		}
		
		public void setValue(int x,int y,T value){
			this.f[x][y] = value;
		}
		public T getValue(int x,int y){
			return this.f[x][y];
		}
		public T minusElement(int x,int y,T t){
			T t1 = this.getValue(x, y);
			Interpreter i = new Interpreter();
			try {
				i.set("t1", t1);
				i.set("t2", t);
				return (T) i.eval("t1-t2");
			} catch (EvalError e) {
				e.printStackTrace();
			}
			
			return null;
		}
		public T plusElement(int x,int y,T t){
			T t1 = this.getValue(x, y);
			Interpreter i = new Interpreter();
			try {
				i.set("t1", t1);
				i.set("t2", t);
				return (T) i.eval("t1+t2");
			} catch (EvalError e) {
				e.printStackTrace();
			}
			
			return null;
		}
		public T multiElement(int x,int y,T t){
			T t1 = this.getValue(x, y);
			Interpreter i = new Interpreter();
			try {
				i.set("t1", t1);
				i.set("t2", t);
				return (T) i.eval("t1*t2");
			} catch (EvalError e) {
				e.printStackTrace();
			}
			
			return null;
		}
		public void trace(){
			System.out.println("************ matrx : [ "+name+" ] ************");
			for(int row=0;row<this.getRowCount();row++){
				for(int colum=0;colum<this.getColumCount();colum++){
					System.out.print(f[row][colum]+"\t");
				}
				System.out.println();
			}
		}
		
		public boolean eachElemntLt(Object t){
			Boolean temp = true;
			for(int row=0;row<this.getRowCount();row++){
				for(int colum=0;colum<this.getColumCount();colum++){
					Interpreter i = new Interpreter();
					try {
						i.set("t1", this.getValue(row, colum));
						i.set("t2", t);
						Boolean o = (Boolean) i.eval("Math.abs(t1)<t2");
						temp = temp&&o;
					} catch (EvalError e) {
						e.printStackTrace();
					}
				}
			}
			return temp;
		}
		
		
		/**
		 * C=A-B
		 * */
		public  Matrix<T> minus(Matrix<T> b){
			T[][] twoDimensionArray = (T[][]) Array.newInstance(f[0][0].getClass(), this.getRowCount(), this.getColumCount());
			Matrix<T> mat = new Matrix<T>(twoDimensionArray);
			if(this.getRowCount()==b.getRowCount() && this.getColumCount()==b.getColumCount()){
				for(int row=0;row<this.getRowCount();row++){
					for(int colum=0;colum<this.getColumCount();colum++){
						T t1 = this.minusElement(row, colum, b.getValue(row, colum));
						mat.setValue(row,colum, t1);
					}
				}
			}else{
				throw new RuntimeException("不支持该种矩阵相加");
			}
			return mat;
		}
		
		
		
		/**
		 * C=A+B
		 * */
		public  Matrix<T> plus(Matrix<T> b){
			T[][] twoDimensionArray = (T[][]) Array.newInstance(f[0][0].getClass(), this.getRowCount(), this.getColumCount());
			Matrix<T> mat = new Matrix<T>(twoDimensionArray);
			if(this.getRowCount()==b.getRowCount() && this.getColumCount()==b.getColumCount()){
				for(int row=0;row<this.getRowCount();row++){
					for(int colum=0;colum<this.getColumCount();colum++){
						T t1 = this.plusElement(row, colum, b.getValue(row, colum));
						mat.setValue(row,colum, t1);
					}
				}
			}else{
				throw new RuntimeException("不支持该种矩阵相加");
			}
			return mat;
		}
		
		/**
		 * C=aA
		 * */
		public Matrix<T> multi(T t){
			T[][] twoDimensionArray = (T[][]) Array.newInstance(f[0][0].getClass(), this.getRowCount(), this.getColumCount());
			Matrix<T> mat = new Matrix<T>(twoDimensionArray);
			for(int row=0;row<this.getRowCount();row++){
				for(int colum=0;colum<this.getColumCount();colum++){
					T t1 = this.multiElement(row, colum, t);
					mat.setValue(row,colum, t1);
				}
			}
			
			return mat;
		}
		/**
		 * C=A*B
		 * */
		public Matrix<T> multi(Matrix<T> b){
			T[][] twoDimensionArray = (T[][]) Array.newInstance(f[0][0].getClass(), this.getRowCount(), b.getColumCount());
			Matrix<T> mat = new Matrix<T>(twoDimensionArray);
			if(this.getColumCount()==b.getRowCount()){
				
				for(int row=0;row<mat.getRowCount();row++){
					for(int colum=0;colum<mat.getColumCount();colum++){
						for(int k=0;k<this.getColumCount();k++){
							T t1 = mat.getValue(row, colum);
							T t2 = this.getValue(row, k);
							T t3 = b.getValue(k, colum);
							Interpreter i = new Interpreter();
							try {
								i.set("t1", (t1==null?0:t1));
								i.set("t2", (t2==null?0:t2));
								i.set("t3", (t3==null?0:t3));
								T temp = (T)i.eval("t1+=t2*t3");
								mat.setValue(row, colum, temp);
							} catch (EvalError e) {
								e.printStackTrace();
							}
						}
					}
				}
				
			}else{
				throw new RuntimeException("不支持该种矩阵相乘");
			}
			
			return mat;
		}
		
		
	}
	
	/**
	 * 终止步数
	 * */
	public static int overSept = 10;
	
	/**
	 * 终止阈值
	 * */
	public static double overThreshold = 0.0008;
	
	
	
	private Matrix<T> S;
	private Matrix<T> U;
	private double alapha = 0.6;
	private int n = 1;
	private Matrix<T> G;
	
	public Matrix<T> getS() {
		return S;
	}
	public void setS(Matrix<T> s) {
		S = s;
	}
	public Matrix<T> getU() {
		return U;
	}
	public void setU(Matrix<T> u) {
		U = u;
	}
	public double getAlapha() {
		return alapha;
	}
	public void setAlapha(double alapha) {
		this.alapha = alapha;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public Matrix<T> getG() {
		return G;
	}
	public void setG(Matrix<T> g) {
		G = g;
	}
	/**
	 * @param 
	 * 		S S矩阵
	 * @param
	 * 		U U矩阵
	 * @param
	 * 		qurr 初始PANGRANK特征值
	 * 
	 * */
	public PageRankMock( Matrix<T> S, Matrix<T>  U,Matrix<T> qurr){
		this.S = S;
		this.U = U;
		n = this.U.getColumCount();
		history = new ArrayList<Matrix<T>>();
		//qcurr = new Matrix<T>((T[][])Array.newInstance(this.S.getValue(0, 0).getClass(), this.S.getRowCount(), 1));
		this.qcurr = qurr;
//		this.S.multi(this.alapha).plus(U.multi((T)(1-this.alapha)/n));
		
		//G=this.alapha*S+(1-this.alapha)/n*U
	}
	
	
	/**
	 * 当前迭代次数
	 * */
	private int currentSept = 0;
	
	/**
	 * 当前状态
	 * */
	private int stat = 0 ;
	
	public static boolean debug = true;
	
	private Matrix<T> qcurr;
	private Matrix<T> qnext;
	
	private Matrix<T> qfinsh;
	private List<Matrix<T>> history ;
	public void excu(){
		//终结条件1
		//超过迭代次数
		if(this.currentSept > this.overSept){
			stat = 1;
			qfinsh=this.qnext;
			return ;
		}

		this.qnext = this.G.multi(this.qcurr);
		this.qnext.setName("第 "+(this.currentSept+1)+" 次迭代 向量");
		this.history.add(this.qnext);
		
		//终结条件2
		//目标向量收敛
		if(this.currentSept!=0 && qnext.minus(qcurr).eachElemntLt(overThreshold)){
			stat = 2;
			this.qfinsh = this.qnext;
			return ;
		}
		//迭代运算
		this.qcurr = this.qnext;
		
		this.currentSept++;
		this.excu();
	}
	public Matrix<T> finsh(){
		return this.qfinsh;
	}
	
	public String getStat(){
		switch(this.stat){
			case 0: return "初始化";
			case 1: return "超出迭代步骤";
			case 2: return "收敛计算";
			default : return "其他";
		}
	}
	
	public List<Matrix<T>> getHistory(){
		return this.history;
	}
	
	public static void main(String[] args) {
		Double[][] S = {{0d,0d,0d,0d},{0.33d,0d,0d,1d},{0.33d,0.5d,0d,0d},{0.33d,0.5d,1d,0d}};
		Matrix<Double> SM = new Matrix<Double>(S);
		SM.setName("S矩阵");
		
		Double[][] U = {{1d,1d,1d,1d},{1d,1d,1d,1d},{1d,1d,1d,1d},{1d,1d,1d,1d}};
		Matrix<Double> UM = new Matrix<Double>(U);
		UM.setName("U矩阵");
		
		
		Double[][] qurr = {{1d},{1d},{1d},{1d}};
		Matrix<Double> qurrM = new Matrix<Double>(qurr);
		qurrM.setName("初始化pagerank向量");
		
		PageRankMock<Double> prm = new PageRankMock<Double>(SM, UM, qurrM);
		prm.setAlapha(0.06);
		
		
		//初始化GOOGLE矩阵
		//G=this.alapha*S+(1-this.alapha)/n*U
		
		Matrix<Double> G = prm.getS().multi(prm.getAlapha()).plus(prm.getU().multi(((1-prm.getAlapha())/prm.getN())));
		prm.setG(G);
		prm.getG().setName("google 矩阵");
		
		prm.overSept = 10000;
		
		System.out.println("迭代终止条件 ："+prm.overSept+"  步 ， 或  收敛阈值 "+prm.overThreshold);
		
		System.out.println("alapha : "+prm.getAlapha());
		
		System.out.println("n : "+prm.getN());
		
		prm.getS().trace();
		
		prm.getU().trace();
		
		prm.getG().trace();
		
		prm.excu();
		
		for(Matrix<Double> item : prm.getHistory()){
			item.trace();
		}
		
		System.out.println("终止状态 ： "+prm.getStat());
		prm.finsh().trace();
		
		
	}
	
	
	public static void main1(String[] args) {
		Double[][] d = {{1d,1d},{2d,2d},{3d,3d}};
		Double[][] d2 = {{1d},{2d}};
		Matrix<Double> md = new Matrix<Double>(d);
		Matrix<Double> md2 = new Matrix<Double>(d2);
		md.trace();
		md2.trace();
//		md.plus(md2);
		
//		md.multi(10d);
		md.trace();
		
//		d = md.getEmptySquare();
		System.out.println(d.length);
		System.out.println(d[0].length);
		
		
		Matrix<Double> c = md.multi(md2);
		c.setName("乘积");
		c.trace();
		
	}

}
