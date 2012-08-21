package cn.gbase;

import java.lang.reflect.Array;

import bsh.EvalError;
import bsh.Interpreter;

public class PageRankMock {
	
	
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
		
		public  Matrix<T> plus(Matrix<T> b){
			if(this.getRowCount()==b.getRowCount() && this.getColumCount()==b.getColumCount()){
				for(int row=0;row<this.getRowCount();row++){
					for(int colum=0;colum<this.getColumCount();colum++){
						T t1 = this.plusElement(row, colum, b.getValue(row, colum));
						this.setValue(row,colum, t1);
					}
				}
			}else{
				throw new RuntimeException("不支持该种矩阵相加");
			}
			return this;
		}
		
		public Matrix<T> multi(T t){
			for(int row=0;row<this.getRowCount();row++){
				for(int colum=0;colum<this.getColumCount();colum++){
					T t1 = this.multiElement(row, colum, t);
					this.setValue(row,colum, t1);
				}
			}
			
			return this;
		}
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
	
	
	
	
	public static void main(String[] args) {
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
