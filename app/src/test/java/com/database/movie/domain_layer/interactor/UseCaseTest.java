/*
 *
 *  * Copyright (c) 2018 Ilanthirayan Paramanathan Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.database.movie.domain_layer.interactor;

import com.database.movie.domain_layer.executor.PostExecutionThread;
import com.database.movie.domain_layer.executor.ThreadExecutor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.TestScheduler;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 22nd of January 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class UseCaseTest {

    private UseCaseTestClass useCase;

    private TestDisposableObserver<Object> testObserver;

    @Mock
    private ThreadExecutor mockThreadExecutor;
    @Mock
    private PostExecutionThread mockPostExecutionThread;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        this.useCase = new UseCaseTestClass(mockThreadExecutor, mockPostExecutionThread);
        this.testObserver = new TestDisposableObserver<>();
        given(mockPostExecutionThread.getScheduler()).willReturn(new TestScheduler());
    }

    @Test
    public void testBuildUseCaseObservableReturnCorrectResult(){
        useCase.execute(testObserver, Params.EMPTY);
        assertThat(testObserver.count).isZero();
    }

    @Test
    public void testSubscriptionWhenExecutingUseCase(){
        useCase.execute(testObserver, Params.EMPTY);
        useCase.dispose();

        assertThat(testObserver.isDisposed()).isTrue();
    }

    @Test
    public void testShouldFailWhenExecutingWithNullObserver(){
        expectedException.expect(NullPointerException.class);
        useCase.execute(null, Params.EMPTY);
    }

    private class UseCaseTestClass extends UseCase<Object, Params>{

        UseCaseTestClass(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
            super(threadExecutor, postExecutionThread);
        }

        @Override
        public Observable<Object> buildUseCaseObservable(Params params) {
            return Observable.empty();
        }

        @Override
        public void execute(DisposableObserver<Object> observer, Params params) {
            super.execute(observer, params);
        }
    }

    private class TestDisposableObserver<T> extends DisposableObserver<T>{

        private int count = 0;

        @Override
        public void onNext(T t) {
            count++;
        }

        @Override
        public void onError(Throwable e) {
            //no-op by default.
        }

        @Override
        public void onComplete() {
            //no-op by default.
        }
    }

    private static class Params{
        private static Params EMPTY = new Params();
        private Params() {}
    }

}