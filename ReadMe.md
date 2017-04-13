## MultiRefreshLoading

This is an android UI displayed by Pagable data from net impleting by fragment.

![Multi-refresh](http://olel07toq.bkt.clouddn.com/multi-refr.gif)           
![Multi-load-more](http://olel07toq.bkt.clouddn.com/multi-foot.gif)

## Basic usage

> gradle

```
compile 'radasm.van:MultiRefreshLoading:0.2.0'
```


#### 1、`BaseAbstractRefreshLoadingFragment`

By using this class , you can easily create a UI supporting both refreshing and loading more.

for example:

```
public class BaseImplFragment extends BaseAbstractRefreshLoadingFragment<Pagnation<MemberBlog>, MemberBlog> {

    @Override
    protected int provideLayoutResId() {
        // provide a layout id,or you can provide 0,we will use a default one.
        return R.layout.fragment_base_recycler_impl;
    }

    @Override
    protected void onRequestData() {
        // call API to get data , for example
        Call<Pagnation<MemberBlog>> memberBlogs = RetrofitClient.service(API.class).getMemberBlogs("akimotomanatsu", 20);
        memberBlogs.enqueue(new Callback<Pagnation<MemberBlog>>() {
            @Override
            public void onResponse(Call<Pagnation<MemberBlog>> call, Response<Pagnation<MemberBlog>> response) {
                onRequestResult(response.body());
            }

            @Override
            public void onFailure(Call<Pagnation<MemberBlog>> call, Throwable t) {
                onRequestResultFail();
            }
        });

    }

    @Override
    protected int provideListViewResId() {
        // you can specify the list used in the UI
        // if you return 0 by default, we will use a default list view implemented by ListView。
        return 0;
    }

    @Override
    protected View getAdapterView(int position, View convertView, ViewGroup parent) {
        // set how to customize the view by adapter

        //Note: if you want to use the Adapter Class,just implement method of {setAdapter} below
        ...
    }

	 // need not to implement
    @Override
    protected BasicAdapter<MemberBlog> setAdapter() {
        return super.setAdapter();
    }
}

``` 

and the layoutResId may be like this in below:

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:dooioo="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <van.tian.wen.library.MultiStateRefreshLayout
        android:id="@+id/validSaleRefreshLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        dooioo:emptyView="@layout/view_default_empty"
        dooioo:footErrorView="@layout/default_item_error_view"
        dooioo:footLoadingView="@layout/view_default_loading"
        dooioo:footSuccessView="@layout/view_defualt_success"
        dooioo:footUnknownView="@layout/view_default_no_more"
        dooioo:listView="@layout/default_list_view"
        dooioo:recyclerView="@layout/base_recycler_view_layout_id"
        dooioo:requestErrorView="@layout/view_default_error"
        dooioo:useRecyclerView="true">

    </van.tian.wen.library.MultiStateRefreshLayout>

</LinearLayout>

```


the attribute below :

- `dooioo:useRecyclerView`
	
	by set 'true',the UI will use `RecyclerView` as the list,otherwise `ListView` will be used.
	
- `dooioo:listView`

	provide a `ListView`, if you did not provide one, we will use a default one.

- `dooioo:recyclerView`

	provide a `RecyclerView`, if you did not provide one, we will use a default one.

- `dooioo:emptyView`

	provide a empty view displayed when the data should be displayed in the content of the list is empty.
	
	absulotely,we have a default one.
	
- `dooioo:requestErrorView`	
	
	provide a error view displayed wher the request of data is error.
	
- `dooioo:footErrorView`

	provide a error view displayed in the foot of the list when loading more.

- `dooioo:footLoadingView`

	provide a loading view displayed in the foot of the list when loading more.

- `dooioo:footSuccessView`

	provide a success view displayed in the foot of the list when loading more.
	
- `dooioo:footUnknownView`

	provide a unknown view displayed in the foot of the list when loading more.
	
	
#### 2、default implement

if you think creating a UI by implementing such many things is some much cumbersome.
	
Do not worry,you can use classes below to have a much better experience。

- `BaseListRefreshLoadingFragment`

```
public class ListFragment extends BaseListRefreshLoadingFragment<Pagnation<MemberBlog>, MemberBlog> {

    @Override
    protected void onRequestData() {
        super.onRequestData();
    }

    @Override
    protected BasicAdapter<MemberBlog> setAdapter() {
        return super.setAdapter();
    }

    @Override
    protected void onListItemClicked(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onListItemLongClicked(AdapterView<?> parent, View view, int position, long id) {

    }
    
}
```

- `BaseRecyclerRefreshingLoadingFragment`

```
public class RecyclerViewFragment extends BaseRecyclerRefreshingLoadingFragment<Pagnation<MemberBlog>, MemberBlog> {

    private MemberBlogAdapter mMemberBlogAdapter;

    @Override
    protected void onRequestData() {
        super.onRequestData();
		
    }

    @Override
    protected BasicRecyclerAdapter<MemberBlog> setRecyclerAdapter() {
   		
    }
    
    
    @Override
    protected void onRecyclerItemClicked(View view, int position) {
        
    }

    @Override
    protected void onRecyclerItemLongClicked(View view, int position) {
        
    }
    
}
```





	
	
	

	






